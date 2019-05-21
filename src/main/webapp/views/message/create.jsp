<%--
 * action-2.jsp
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

<form:form action="message/create.do" modelAttribute="mesage">

    <security:authorize
            access="isAuthenticated()">

        <%-- Subject --%>
        <acme:textbox code="message.subject" path="subject"/>
        <br>

        <spring:message code="message.priority"/>
        <form:select path="priority" multiple="false">
            <form:option value="LOW"><spring:message code="message.priority.low"/></form:option>
            <form:option value="MEDIUM"><spring:message code="message.priority.medium"/></form:option>
            <form:option value="HIGH"><spring:message code="message.priority.high"/></form:option>
        </form:select>
        <form:errors class="error" path="priority"/>
        <br>

        <%-- Body --%>
        <acme:textarea code="message.body" path="body"/>
        <br>

        <%-- Recipients --%>
        <acme:select items="${actorList}" itemLabel="name" code="message.recipients" path="recipients"/>
        <br>

        <%-- Tags --%>
        <acme:textbox code="message.tags" path="tags"/>
        <br>

        <%-- Buttons --%>
        <acme:submit name="save" code="message.send"/>

        <acme:cancel url="messageBox/list.do" code="message.cancel"/>

    </security:authorize>
</form:form>