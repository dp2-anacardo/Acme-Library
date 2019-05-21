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
    <display:table pagesize="5" name="transactions" requestURI="${requestURI}" id="row">

        <spring:message code="transaction.show" var="show" />
        <display:column title="${delete}">
                <a href="transaction/reader/show.do?transactionId=${row.id}">
                    <spring:message code="transaction.show" /></a>
        </display:column>

        <spring:message code="transaction.book" var="book" />
        <display:column property="book.title" title="${book}"/>

        <spring:message code="transaction.state" var="state" />
        <display:column title="${state}">
            <jstl:choose>
                <jstl:when test="${row.isFinished == true}">
                    <spring:message code="transaction.sold" var="sold" />
                    <jstl:out value="${sold}"></jstl:out>
                </jstl:when>
                <jstl:otherwise>
                    <spring:message code="transaction.onSale" var="sold" />
                    <jstl:out value="${sold}"></jstl:out>
                </jstl:otherwise>
            </jstl:choose>
        </display:column>

        <spring:message code="transaction.price" var="price" />
        <display:column property="price" title="${price}"/>

        <spring:message code="transaction.moment" var="moment" />
        <display:column title="${moment}">
            <jstl:if test="${row.moment != null}">
                <jstl:out value="${row.moment}"></jstl:out>
            </jstl:if>
        </display:column>

        <spring:message code="transaction.buyer" var="buyer" />
        <display:column title="${buyer}">
            <jstl:if test="${row.buyer != null}">
                <jstl:out value="${row.buyer.name}"></jstl:out>
            </jstl:if>
        </display:column>

        <spring:message code="transaction.delete" var="delete" />
        <display:column title="${delete}">
            <jstl:if test="${row.isFinished == false}">
                <a href="transaction/reader/delete.do?transactionId=${row.id}">
                    <spring:message code="transaction.delete" /></a>
            </jstl:if>
        </display:column>


    </display:table>



    <acme:cancel url="transaction/reader/createSale.do" code="transaction.create" />
</security:authorize>
