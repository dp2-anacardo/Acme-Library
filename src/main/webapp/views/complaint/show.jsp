<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:showtext fieldset="true" code="complaint.moment" value="${complaint.moment}"/>
<acme:showtext fieldset="true" code="complaint.body" value="${complaint.body}"/>

<fieldset><legend><spring:message code="complaint.attachments"/></legend>
    <jstl:forEach items="${complaint.attachments}" var="url">
        <jstl:out value="${url.link}"></jstl:out>
    </jstl:forEach>
</fieldset>

<security:authorize access="hasRole('READER')">
    <acme:cancel url="complaint/reader/list.do" code="complaint.back"/>
</security:authorize>

<security:authorize access="hasRole('REFEREE')">
    <acme:cancel url="complaint/referee/list.do" code="complaint.back"/>
</security:authorize>