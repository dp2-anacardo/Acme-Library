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


<display:table name="messages" id="row" requestURI="message/list.do"
	pagesize="5" class="displaytag">


	<spring:message code="message.subject" var="subjectHeader" />
	<display:column property="subject" title="${subjectHeader}" />

	<spring:message code="message.sender" var="senderHeader" />
	<display:column property="sender.name" title="${senderHeader}"
		sortable="true" />

	<spring:message code="message.priority" var="priorityHeader" />
	<display:column property="priority" title="${priorityHeader}"
					sortable="true" />

	<!-- Display -->
	<spring:message code="message.display" var="display"/>
	<display:column title="${display}">
		<a
			href="message/display.do?messageID=${row.id}&messageBoxID=${messageBox}">
			<spring:message code="message.display" />
		</a>
	</display:column>

</display:table>

<security:authorize
	access="isAuthenticated()">
	<acme:cancel url="message/create.do" code="message.create"/>
</security:authorize>

<acme:cancel url="messageBox/list.do" code="messageBox.goBack"/>
