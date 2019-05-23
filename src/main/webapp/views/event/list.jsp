<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authorize access="hasRole('ORGANIZER')">
    <display:table name="events" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

        <spring:message code="event.title" var="columTitle"/>
        <display:column title="${columTitle}">
            <jstl:out value="${row.title}"/>
        </display:column>

        <spring:message code="event.date" var="columTitle"/>
        <display:column title="${columTitle}">
            <jstl:out value="${row.date}"/>
        </display:column>

        <spring:message code="event.maximumCapacity" var="columTitle"/>
        <display:column title="${columTitle}">
            <jstl:out value="${row.maximumCapacity}"/>
        </display:column>

        <spring:message code="event.status" var="columTitle"/>
        <display:column title="${columTitle}">
            <jstl:if test="${row.isFinal == false}">
                <spring:message code="event.draft" var = "Draft"/>
                <jstl:out value="${Draft}"/>
            </jstl:if>
            <jstl:if test="${row.isFinal == true}">
                <spring:message code="event.final" var = "Final"/>
                <jstl:out value="${Final}"/>
            </jstl:if>
        </display:column>

        <display:column>
            <a href="event/organizer/show.do?eventId=${row.id}">
                <spring:message code="event.show"/>
            </a>
        </display:column>

        <display:column>
            <jstl:if test="${row.isFinal == false}">
                <a href="event/organizer/edit.do?eventId=${row.id}">
                    <spring:message code="event.edit"/>
                </a>
            </jstl:if>
            </display:column>
    </display:table>

    <input type="button" value="<spring:message code="event.create" />"
           onclick="javascript: relativeRedir('event/organizer/create.do');" />

</security:authorize>