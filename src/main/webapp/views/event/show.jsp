<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ORGANIZER')">

    <display:table name="event" id="row" requestURI="event/show.do"
                   class="displaytag">
        <spring:message code="event.title" var="title"/>
        <display:column property="title" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="event" id="row" requestURI="event/show.do"
                   class="displaytag">
        <spring:message code="event.description" var="title"/>
        <display:column property="description" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="event" id="row" requestURI="event/show.do"
                   class="displaytag">
        <spring:message code="event.date" var="title"/>
        <display:column property="date" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="event" id="row" requestURI="event/show.do"
                   class="displaytag">
        <spring:message code="event.address" var="title"/>
        <display:column property="address" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="event" id="row" requestURI="event/show.do"
                   class="displaytag">
        <spring:message code="event.maximumCapacity" var="title"/>
        <display:column property="maximumCapacity" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="event" id="row" requestURI="event/show.do"
                   class="displaytag">
        <spring:message code="event.actualCapacity" var="title"/>
        <display:column property="actualCapacity" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="event" id="row" requestURI="event/show.do"
                   class="displaytag">
        <spring:message code="event.status" var="title"/>
        <display:column title="${title}">
            <jstl:if test="${row.isFinal == false}">
                <spring:message code="event.draft" var = "Draft"/>
                <jstl:out value="${Draft}"/>
            </jstl:if>
            <jstl:if test="${row.isFinal == true}">
                <spring:message code="event.final" var = "Final"/>
                <jstl:out value="${Final}"/>
            </jstl:if>
        </display:column>
    </display:table>

    <display:table name="event" id="row" requestURI="event/show.do"
                   class="displaytag">
        <spring:message code="event.organizer" var="title"/>
        <display:column  title="${title}" sortable="false">
            <jstl:out value="${row.organizer.name}"/>
        </display:column>
    </display:table>


    <h2><spring:message code="event.registers"/></h2>
    <display:table name="registers" id="row" requestURI="event/show.do"
                    class="displaytag">

        <spring:message code = "event.register.date" var = "title"/>
        <display:column  title="${title}" sortable="true">
            <jstl:out value="${row.moment}"/>
        </display:column>

        <spring:message code = "event.register.reader" var = "title"/>
        <display:column  title="${title}" sortable="false">
            <jstl:out value="${row.reader.name}"/>
        </display:column>


    </display:table>

    <acme:cancel code="event.back" url="/event/organizer/list.do"/>
</security:authorize>