<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:showtext fieldset="true" code="event.title" value="${event.title}"/>
<acme:showtext fieldset="true" code="event.description" value="${event.description}"/>
<acme:showtext fieldset="true" code="event.date" value="${event.date}"/>
<acme:showtext fieldset="true" code="event.address" value="${event.address}"/>
<acme:showtext fieldset="true" code="event.maximumCapacity" value="${event.maximumCapacity}"/>
<acme:showtext fieldset="true" code="event.actualCapacity" value="${event.actualCapacity}"/>
<fieldset><legend><spring:message code="event.organizer"/></legend>
    <a href="profile/show.do?actorId=${event.organizer.id}">
        <spring:message code="event.organizer"/></a>
</fieldset>

<jstl:if test="${not empty sponsorshipBanner}">
    <img src="${sponsorshipBanner}"/>
</jstl:if>
<br>

    <acme:cancel code="event.back" url="/event/list.do"/>