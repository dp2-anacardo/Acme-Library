<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ORGANIZER')">
    <form:form action="event/organizer/edit.do" modelAttribute="event">

        <form:input type ="hidden" path="id" readonly="true"/>

        <acme:textbox code="event.title" path="title" />

        <acme:textbox code="event.description" path="description" />

        <acme:textbox code="event.date" path="date" placeholder="dd/MM/yyyy HH:mm"/>

        <acme:textbox code="event.address" path="address" />

        <acme:textbox code="event.maximumCapacity" path="maximumCapacity" />

        <acme:submit name="saveDraft" code="event.saveDraft"/>

        <acme:submit name="saveFinal" code="event.saveFinal"/>

        <jstl:if test="${event.id != 0}">
            <acme:cancel code="event.delete" url="event/organizer/delete.do?eventId=${event.id}"/>
        </jstl:if>

        <acme:cancel code="event.back" url="/event/organizer/list.do"/>


    </form:form>
</security:authorize>