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

        <spring:message code="transaction.book" var="book" />
        <display:column property="book.title" title="${book}"/>

        <spring:message code="transaction.delete" var="delete" />
        <display:column title="${delete}">
            <jstl:if test="${row.isFinished == false}">
                <a href="transaction/reader/delete.do?transactionId=${row.id}">
                    <spring:message code="transaction.delete" /></a>
            </jstl:if>
        </display:column>

        <spring:message code="complaint.create" var="complaint" />
        <display:column title="${complaint}">
            <jstl:if test="${row.isFinished == true}">
                <a href="complaint/reader/create.do?transactionId=${row.id}">
                    <spring:message code="complaint.create" /></a>
            </jstl:if>
        </display:column>

        <spring:message code="transaction.offers" var="offers" />
        <display:column title="${offers}">
                <a href="offer/reader/list.do?transactionId=${row.id}">
                    <spring:message code="transaction.offers" /></a>
        </display:column>



    </display:table>



    <acme:cancel url="transaction/reader/createExchange.do" code="transaction.createExchange" />
</security:authorize>