<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('READER')">
	<display:table pagesize="5" name="transactions" requestURI="finder/reader/list.do" id="row">

		<!-- Action links -->

		<!-- Attributes -->

		<spring:message code="transaction.book.title" var="title" />
		<display:column title="${title}">
			<jstl:out value="${row.book.title}"></jstl:out>
		</display:column>

		<spring:message code="transaction.moment" var="moment" />
		<display:column property="moment" title="${moment}" format="{0,date,dd/MM/yyyy HH:mm}"/>

		<spring:message code="transaction.book.status" var="status" />
		<display:column title="${status}">
			<jstl:out value="${row.book.status}"></jstl:out>
		</display:column>

		<spring:message code="transaction.book.category" var="category" />
		<display:column title="${category}">
			<jstl:out value="${row.book.category}"></jstl:out>
		</display:column>

		<spring:message code="transaction.price" var="price" />
		<jstl:if test="${row.isSale = true}">
			<display:column property="price" title="${price}"/>
		</jstl:if>

		<display:column>
			<jstl:choose>
				<jstl:when test="${row.isSale = true}">
					<a href="/transaction/reader/showSaleR.do?transactionId=${row.id}">
				</jstl:when>
				<jstl:otherwise>
					<a href="/transaction/reader/showExchangeR.do?transactionId=${row.id}">
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

	</display:table>
</security:authorize>