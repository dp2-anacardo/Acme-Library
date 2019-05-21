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
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<display:table name="mesage" id="row" requestURI="message/display.do"
	class="displaytag">
	<spring:message code="message.subject" var="subjectHeader" />
	<display:column property="subject" title="${subjectHeader}"
		sortable="false" />
</display:table>

<display:table name="mesage" id="row" requestURI="message/display.do"
	class="displaytag">
	<spring:message code="message.sender" var="senderHeader" />
	<display:column property="sender.email" title="${senderHeader}"
		sortable="false" />
</display:table>

<display:table name="mesageRecipients" id="row"
	requestURI="message/display.do" class="displaytag">
	<spring:message code="message.recipients" var="recipientHeader" />
	<display:column property="email" title="${recipientHeader}"
		sortable="false" />
</display:table>

<display:table name="mesage" id="row" requestURI="message/display.do"
	class="displaytag">
	<spring:message code="message.priority" var="priorityHeader" />
	<jstl:choose>
		<jstl:when test="${row.priority == 'HIGH'}">
			<display:column title="${priorityHeader}" sortable="false"><spring:message code="message.priority.high"/></display:column>
		</jstl:when>
		<jstl:when test="${row.priority == 'MEDIUM'}">
			<display:column title="${priorityHeader}" sortable="false"><spring:message code="message.priority.medium"/></display:column>
		</jstl:when>
		<jstl:when test="${row.priority == 'LOW'}">
			<display:column title="${priorityHeader}" sortable="false"><spring:message code="message.priority.low"/></display:column>
		</jstl:when>
	</jstl:choose>
</display:table>

<display:table name="mesage" id="row" requestURI="message/display.do"
	class="displaytag">
	<spring:message code="message.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}"
		sortable="false" />
</display:table>

<display:table name="mesage" id="row" requestURI="message/display.do"
	class="displaytag">
	<spring:message code="message.body" var="bodyHeader" />
	<display:column property="body" title="${bodyHeader}" sortable="false" />
</display:table>

<display:table name="mesage" id="row" requestURI="message/display.do"
	class="displaytag">
	<spring:message code="message.tags" var="tagsHeader" />
	<display:column title="${tagsHeader}">
		<jstl:forEach var="text" items="${row.tags}" varStatus="loop">
			${text}${!loop.last ? ',' : ''}&nbsp
		</jstl:forEach>
	</display:column>
</display:table>

<jstl:if test="${!empty messageBoxes}">
	<select id="actorBoxes">
		<jstl:forEach items="${messageBoxes}" var="msgBox">
			<option value="${msgBox.id}">${msgBox.name}</option>
		</jstl:forEach>
	</select>

	<acme:cancel id="moveButton" url="message/move.do?messageID=${row.id}&srcBoxID=${messageBoxID}&destBoxID=" code="message.move"/>
	
	<acme:cancel id="copyButton" url="message/copy.do?messageID=${row.id}&srcBoxID=${messageBoxID}&destBoxID=" code="message.copy"/>
	
	<br>
	<br>
</jstl:if>

<acme:cancel url="message/delete.do?messageID=${row.id}&messageBoxID=${messageBoxID}" code="message.delete"/>
<acme:cancel url="message/list.do?messageBoxID=${messageBoxID}" code="message.goBack" />

<script type="text/javascript">
	$(document).ready(function() {
		changeLink();
		$('#actorBoxes').change(function() {
			changeLink();
		});
	});

	function changeLink() {
		var box = document.getElementById("actorBoxes").selectedOptions[0].value;
		var strBox = box.toString();
		var moveUrl = "javascript: relativeRedir('message/move.do?messageID=${row.id}&srcBoxID=${messageBoxID}&destBoxID=";
		var copyUrl = "javascript: relativeRedir('message/copy.do?messageID=${row.id}&srcBoxID=${messageBoxID}&destBoxID=";
		document.getElementById("moveButton").setAttribute("onclick", moveUrl + strBox + "')");
		document.getElementById("copyButton").setAttribute("onclick", copyUrl + strBox + "')");
	}
</script>