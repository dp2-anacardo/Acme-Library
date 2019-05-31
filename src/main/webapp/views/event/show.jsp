<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ORGANIZER')">

    <acme:showtext fieldset="true" code="event.title" value="${event.title}"/>
    <acme:showtext fieldset="true" code="event.description" value="${event.description}"/>
    <acme:showtext fieldset="true" code="event.date" value="${event.date}"/>
    <acme:showtext fieldset="true" code="event.address" value="${event.address}"/>
    <acme:showtext fieldset="true" code="event.maximumCapacity" value="${event.maximumCapacity}"/>
    <acme:showtext fieldset="true" code="event.actualCapacity" value="${event.actualCapacity}"/>

    <fieldset><legend><b> <spring:message code="event.status"/> </b></legend>
        <jstl:choose>
            <jstl:when test="${event.isFinal == false}">
                <spring:message code="event.draft"/>
               </jstl:when>

            <jstl:when test="${event.isFinal == true}">
                <spring:message code="event.final"/>
            </jstl:when>
        </jstl:choose>
    </fieldset>

    <acme:showtext fieldset="true" code="event.organizer" value="${event.organizer.name}"/>
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