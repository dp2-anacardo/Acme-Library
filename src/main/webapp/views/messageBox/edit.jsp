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


<form:form action="messageBox/edit.do" modelAttribute="messageBox">

    <%--  Hidden properties --%>
    <form:hidden path="id"/>

    <%-- Name--%>
    <acme:textbox code="messageBox.name" path="name"/>
    <br>

    <%-- Buttons --%>
    <security:authorize access="isAuthenticated()">
        <acme:submit name="save" code="messageBox.save"/>

        <acme:cancel url="messageBox/list.do" code="messageBox.cancel"/>


        <jstl:if
                test="${messageBox.id != 0 && messageBox.isSystem == false && (empty messageBox.messages)}">
            <acme:submit name="delete" code="messageBox.delete"/>
        </jstl:if>

    </security:authorize>

</form:form>