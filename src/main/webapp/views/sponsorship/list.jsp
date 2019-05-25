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
    <display:table name="sponsorships" id="row" requestURI="${requestURI}"
                   pagesize="5" class="displaytag">

        <spring:message code="sponsorship.event" var="eventHeader"/>
        <display:column property="event.title" title="${eventHeader}"/>

        <spring:message code="sponsorship.targetURL" var="targetURL"/>
        <display:column title="${targetURL}">
            <a href="event/show.do?eventId=${row.event.id}">Target URL</a>
        </display:column>

        <spring:message code="sponsorship.status" var="status" />
        <display:column title="${status}">
            <jstl:if test="${row.status eq false}">
                <spring:message code="sponsorship.status.off" />
            </jstl:if>
            <jstl:if test="${row.status eq true}">
                <spring:message code="sponsorship.status.on" />
            </jstl:if>
        </display:column>

        <spring:message code="sponsorship.show" var="showHeader"/>
        <display:column title="${showHeader}">
            <a href="sponsorship/sponsor/show.do?sponsorshipId=${row.id}"> <spring:message
                    code="sponsorship.show"/></a>
        </display:column>

        <spring:message code="sponsorship.update" var="udpateHeader"/>
        <display:column title="${udpateHeader}">
            <a href="sponsorship/sponsor/update.do?sponsorshipId=${row.id}"> <spring:message
                    code="sponsorship.update"/></a>
        </display:column>

        <spring:message code="sponsorship.switch" var="switchSponsorship" />
        <display:column title="${switchSponsorship}">
            <jstl:choose>
                <jstl:when test="${row.status eq false and row.creditCard.expirationYear lt date}">
                    <spring:message code="sponsorship.creditCard.expirate"/>
                </jstl:when>
                <jstl:when test="${row.status eq false and row.creditCard.expirationYear gt date}">
                    <acme:cancel url='sponsorship/sponsor/activate.do?sponsorshipId=${row.id}'
                                 code="sponsorship.activate" />
                </jstl:when>
                <jstl:when test="${row.status eq true}">
                    <acme:cancel
                            url='sponsorship/sponsor/desactivate.do?sponsorshipId=${row.id}'
                            code="sponsorship.desactivate" />
                </jstl:when>
            </jstl:choose>
        </display:column>
    </display:table>

    <div>
        <acme:cancel url="sponsorship/sponsor/create.do" code="sponsorship.create"/>
        <acme:cancel url="/" code="sponsorship.back"/>
    </div>
</security:authorize>