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

<display:table name="messageBoxes" id="row"
	requestURI="messageBox/list.do" pagesize="5" class="displaytag">


	<spring:message code="messageBox.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" />

	<spring:message code="messageBox.display" var="displayHeader" />
	<display:column title="${displayHeader}">
		<a href="message/list.do?messageBoxID=${row.id}"> <spring:message
				code="messageBox.display" /></a>
	</display:column>

	<spring:message code="messageBox.edit" var="editHeader" />
	<display:column title="${editHeader}">
		<jstl:if test="${row.isSystem == false}">
			<a href="messageBox/edit.do?messageBoxID=${row.id}"> <spring:message
					code="messageBox.edit" /></a>
		</jstl:if>
	</display:column>

</display:table>

<security:authorize
	access="isAuthenticated()">
	<acme:cancel url="messageBox/create.do" code="messageBox.create" />
	<acme:cancel url="message/create.do" code="message.create" />
</security:authorize>