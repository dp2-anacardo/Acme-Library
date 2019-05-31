<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('READER')">
	<display:table pagesize="5" name="transactions" requestURI="finder/reader/list.do" id="row">

		<!-- Action links -->

		<!-- Attributes -->

		<spring:message code="transaction.book.title" var="title" />
		<display:column title="${title}">
			<jstl:out value="${row.book.title}"></jstl:out>
		</display:column>

        <display:column title="ISBN">
            <jstl:out value="${row.book.isbn}"></jstl:out>
        </display:column>

		<spring:message code="transaction.book.status" var="status" />
		<display:column title="${status}">
            <jstl:choose>
                <jstl:when test="${row.book.status == 'VERY GOOD'}">
                    <spring:message code="transaction.book.VERYGOOD" var="veryGood" />
                    <jstl:out value="${veryGood}"/>
                </jstl:when>
                <jstl:when test="${row.book.status == 'GOOD'}">
                    <spring:message code="transaction.book.GOOD" var="good" />
                    <jstl:out value="${good}"/>
                </jstl:when>
                <jstl:when test="${row.book.status == 'BAD'}">
                    <spring:message code="transaction.book.BAD" var="bad" />
                    <jstl:out value="${bad}"/>
                </jstl:when>
                <jstl:when test="${row.book.status == 'VERY BAD'}">
                    <spring:message code="transaction.book.VERYBAD" var="veryBad" />
                    <jstl:out value="${veryBad}"/>
                </jstl:when>
            </jstl:choose>
		</display:column>

		<spring:message code="transaction.price" var="price" />
        <display:column title="${price}">
		<jstl:if test="${row.isSale == true}">
            <jstl:out value="${row.price}"></jstl:out>
		</jstl:if>
        </display:column>

		<spring:message code="transaction.show" var="show" />
		<display:column title="${show}">
            <acme:cancel url="/transaction/show.do?transactionId=${row.id}" code="transaction.show"/>&nbsp
		</display:column>

	</display:table>
</security:authorize>