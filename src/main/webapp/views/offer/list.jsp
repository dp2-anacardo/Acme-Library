<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasAnyRole('READER')">
    <display:table pagesize="5" name="offers" requestURI="${requestURI}" id="row">

        <spring:message code="transaction.show" var="show" />
        <display:column title="${delete}">
            <a href="offer/reader/show.do?offerId=${row.id}">
                <spring:message code="transaction.show" /></a>
        </display:column>

        <spring:message code="transaction.book" var="book" />
        <display:column property="book.title" title="${book}"/>

        <spring:message code="transaction.state" var="state" />
        <display:column property="status" title="${state}"></display:column>



        <spring:message code="transaction.moment" var="moment" />
        <display:column property="moment" title="${moment}"/>

        <jstl:if test="${bidder == false}">
        <spring:message code="offer.bidder" var="buyer" />
        <display:column title="${buyer}">
            <a href="profile/show.do?actorId=${row.reader.id}">
                    ${row.reader.name}</a>
        </display:column>

        </jstl:if>


        <jstl:if test="${bidder == true}">
            <spring:message code="transaction.seller" var="buyer" />
            <display:column title="${buyer}">
                <a href="profile/show.do?actorId=${row.transaction.seller.id}">
                        ${row.transaction.seller.name}</a>
            </display:column>

        </jstl:if>

        <spring:message code="offer.exchange" var="buyer" />
        <display:column title="${buyer}">
            <a href="transaction/show.do?actorId=${row.transaction.id}">
                    ${buyer}</a>
        </display:column>

    </display:table>
</security:authorize>
