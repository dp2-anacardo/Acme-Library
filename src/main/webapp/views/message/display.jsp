<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:showtext code="message.subject" value="${mesage.subject}" fieldset="true"/>
<br>

<acme:showtext code="message.sender" value="${mesage.sender.email}" fieldset="true"/>
<br>

<fieldset>
    <legend><b><spring:message code="message.recipients"/></b></legend>
    <jstl:forEach var="recipient" items="${mesage.recipients}" varStatus="loop">
        ${recipient.email}<br>
    </jstl:forEach>
</fieldset>
<br>

<fieldset>
    <legend><b><spring:message code="message.priority"/></b></legend>
    <jstl:choose>
        <jstl:when test="${mesage.priority == 'HIGH'}">
            <spring:message code="message.priority.high"/>
        </jstl:when>
        <jstl:when test="${mesage.priority == 'MEDIUM'}">
            <spring:message code="message.priority.medium"/>
        </jstl:when>
        <jstl:when test="${mesage.priority == 'LOW'}">
            <spring:message code="message.priority.low"/>
        </jstl:when>
    </jstl:choose>
</fieldset>
<br>

<acme:showtext code="message.moment" value="${mesage.moment}" fieldset="true"/>
<br>

<acme:showtext code="message.body" value="${mesage.body}" fieldset="true"/>
<br>

<fieldset>
    <legend><b><spring:message code="message.tags"/></b></legend>
    <jstl:forEach var="text" items="${message.tags}" varStatus="loop">
        ${text}${!loop.last ? ',' : ''}&nbsp
    </jstl:forEach>
</fieldset>
<br>

<jstl:if test="${!empty messageBoxes}">
    <select id="actorBoxes">
        <jstl:forEach items="${messageBoxes}" var="msgBox">
            <option value="${msgBox.id}">${msgBox.name}</option>
        </jstl:forEach>
    </select>

    <acme:cancel id="moveButton" url="message/move.do?messageID=${mesage.id}&srcBoxID=${messageBoxID}&destBoxID="
                 code="message.move"/>

    <acme:cancel id="copyButton" url="message/copy.do?messageID=${mesage.id}&srcBoxID=${messageBoxID}&destBoxID="
                 code="message.copy"/>

    <br>
    <br>
</jstl:if>

<acme:cancel url="message/delete.do?messageID=${mesage.id}&messageBoxID=${messageBoxID}" code="message.delete"/>
<acme:cancel url="message/list.do?messageBoxID=${messageBoxID}" code="message.goBack"/>

<script type="text/javascript">
    $(document).ready(function () {
        changeLink();
        $('#actorBoxes').change(function () {
            changeLink();
        });
    });

    function changeLink() {
        var box = document.getElementById("actorBoxes").selectedOptions[0].value;
        var strBox = box.toString();
        var moveUrl = "javascript: relativeRedir('message/move.do?messageID=${mesage.id}&srcBoxID=${messageBoxID}&destBoxID=";
        var copyUrl = "javascript: relativeRedir('message/copy.do?messageID=${mesage.id}&srcBoxID=${messageBoxID}&destBoxID=";
        document.getElementById("moveButton").setAttribute("onclick", moveUrl + strBox + "')");
        document.getElementById("copyButton").setAttribute("onclick", copyUrl + strBox + "')");
    }
</script>