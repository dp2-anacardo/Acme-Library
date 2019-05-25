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

<display:table name="events" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

    <spring:message code="event.title" var="title"/>
    <display:column title="${title}">
        <jstl:out value="${row.title}"/>
    </display:column>

    <spring:message code="event.date" var="date"/>
    <display:column title="${date}">
        <jstl:out value="${row.date}"/>
    </display:column>

    <spring:message code="event.maximumCapacity" var="maximumCapacity"/>
    <display:column title="${maximumCapacity}">
        <jstl:out value="${row.maximumCapacity}"/>
    </display:column>

    <spring:message code="event.actualCapacity" var="actualCapacity"/>
    <display:column title="${actualCapacity}">
        <jstl:out value="${row.actualCapacity}"/>
    </display:column>

    <spring:message code="event.show" var="show"/>
    <display:column title="${show}">
        <a href="event/show.do?eventId=${row.id}">
            <spring:message code="event.show"/>
        </a>
    </display:column>

    <security:authorize access="hasRole('READER')">
        <spring:message code="event.checkIn" var="show"/>
        <display:column title="${show}">
            <acme:cancel code="event.checkIn" url="register/reader/checkIn.do?eventId=${row.id}"/>
        </display:column>
    </security:authorize>
    <acme:cancel code="event.back" url="/"/>

</display:table>
