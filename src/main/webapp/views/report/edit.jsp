<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

    <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
    <%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
    <%@taglib prefix="display" uri="http://displaytag.sf.net" %>
    <%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="report/referee/edit.do" modelAttribute="report">

    <form:hidden path="id" readOnly = "true"/>
    <input type="hidden" name="complaintId" value="${complaintId}" readonly>

    <acme:textbox code="report.description" path="description"/>
    <form:errors cssClass="error" path="description" />
    <br>

    <acme:textarea code="report.attachments" path="attachments" />
    <jstl:if test="${not empty attachmentError }">
        <p class="error">${attachmentError }</p>
    </jstl:if>
    <br/>

    <spring:message code="report.isFinal"/>
    <form:select path="isFinal" multiple="false">
        <form:option value="0"><spring:message code="report.draft"/></form:option>
        <form:option value="1"><spring:message code="report.final"/></form:option>
    </form:select>
    <form:errors class="error" path="isFinal"/>
    <br>

    <acme:submit name="save" code="report.save"/>

    <jstl:if test="${row.id == 0}">
    <acme:cancel url="complaint/referee/list.do" code="report.cancel"/>
    </jstl:if>

    <jstl:if test="${row.id != 0}">
        <acme:cancel url="complaint/referee/list.do" code="report.cancel"/>
    </jstl:if>

</form:form>