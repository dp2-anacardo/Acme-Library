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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<display:table name="actors" id="row" requestURI="${requestURI}"
               pagesize="5" class="displaytag">

    <spring:message code="actor.show" var="show"/>
    <display:column title="${show}">
        <acme:cancel url='administrator/management/showActor.do?actorId=${row.id}' code="actor.show"/>
    </display:column>

    <spring:message code="actor.name" var="name"/>
    <display:column property="name" title="${name}"/>

    <spring:message code="actor.email" var="email"/>
    <display:column property="email" title="${email}"/>

    <spring:message code="actor.phoneNumber" var="phoneNumber"/>
    <display:column property="phoneNumber" title="${phoneNumber}"/>

    <spring:message code="actor.isSpammer" var="isSpammer"/>
    <display:column title="${isSpammer}">
        <jstl:if test="${row.isSuspicious == null}">
            <jstl:out value="N/A"></jstl:out>
        </jstl:if>
        <jstl:if test="${row.isSuspicious != null}">
            <jstl:out value="${row.isSuspicious}"></jstl:out>
        </jstl:if>
    </display:column>

    <spring:message code="actor.ban" var="ban"/>
    <display:column title="${ban}">
        <jstl:if
                test="${row.isSuspicious == true && row.isBanned == false}">
            <acme:cancel url='administrator/management/ban.do?actorId=${row.id}'
                         code="administrator.ban"/>
        </jstl:if>
        <jstl:if
                test="${row.isSuspicious == true && row.isBanned == true}">
            <acme:cancel url='administrator/management/unban.do?actorId=${row.id}'
                         code="administrator.unban"/>
        </jstl:if>
    </display:column>
</display:table>

<jstl:if test="${not empty deletedBooks}">
    <b><spring:message code="administrator.deletedBooks"/><jstl:out value="${deletedBooks}"/></b>
    <br>
    <br>
</jstl:if>

<jstl:if test="${not empty deactivateSponsorships}">
    <b><spring:message code="administrator.deactivateSponsorships"/><jstl:out value="${deactivateSponsorships}"/></b>
    <br>
    <br>
</jstl:if>

<acme:cancel url="administrator/management/calculateSpam.do"
             code="administrator.calculateSpam"/>

<acme:cancel url="administrator/management/calculateScore.do"
             code="administrator.calculateScore"/>

<acme:cancel url="administrator/management/deactivateExpiredCreditCards.do"
             code="administrator.deactivateCreditCards"/>

<acme:cancel url="administrator/management/deleteInactiveBooks.do"
             code="administrator.deleteInactiveBooks"/>

<acme:cancel url="/" code="messageBox.goBack"/>