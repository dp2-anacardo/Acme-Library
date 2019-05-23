<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('SPONSOR')">

    <acme:showtext code="event.title" value="${sponsorship.event.title}" fieldset="true"/>
    <br>

    <fieldset>
        <legend><b><spring:message code="sponsorship.targetURL"/></b></legend>
        <a href="event/show.do?eventId=${row.event.id}">Target URL</a>
    </fieldset>
    <br>

    <fieldset>
        <legend><b><spring:message code="sponsorship.creditCard"/></b></legend>
        <acme:showtext code="sponsorship.creditCard.holderName" value="${sponsorship.creditCard.holder}" fieldset="true"/>
        <acme:showtext code="sponsorship.creditCard.brandName" value="${sponsorship.creditCard.brandName}" fieldset="true"/>
        <acme:showtext code="sponsorship.creditCard.number" value="${sponsorship.creditCard.number}" fieldset="true"/>
        <acme:showtext code="sponsorship.creditCard.expiration" value="${sponsorship.creditCard.expirationYear}" fieldset="true"/>
        <acme:showtext code="sponsorship.creditCard.cvvCode" value="${sponsorship.creditCard.cvv}" fieldset="true"/>
    </fieldset>
    <br>

    <fieldset>
        <legend><b><spring:message code="sponsorship.banner"/></b></legend>
        <img src="${sponsorship.banner}"/>
    </fieldset>
    <br>

    <acme:cancel url="sponsorship/sponsor/update.do?sponsorshipId=${row.id}"
                 code="sponsorship.update"/>

    <acme:cancel url="sponsorship/sponsor/list.do" code="sponsorship.back"/>


</security:authorize>