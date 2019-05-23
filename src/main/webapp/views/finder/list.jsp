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

		<spring:message code="transaction.moment" var="moment" />
        <display:column title="${moment}" format="{0,date,dd/MM/yyyy HH:mm}">
            <jstl:out value="${row.moment}"></jstl:out>
        </display:column>

		<spring:message code="transaction.book.status" var="status" />
		<display:column title="${status}">
			<jstl:out value="${row.book.status}"></jstl:out>
		</display:column>

		<spring:message code="transaction.price" var="price" />
        <display:column title="${price}">
		<jstl:if test="${row.isSale = true}">
            <jstl:out value="${row.price}"></jstl:out>
		</jstl:if>
        </display:column>

		<display:column>
			<jstl:choose>
				<jstl:when test="${row.isSale = true}">
                    <acme:cancel url="/transaction/reader/showSaleR.do?transactionId=${row.id}" code="transaction.show"/>&nbsp
				</jstl:when>
				<jstl:otherwise>
                    <acme:cancel url="/transaction/reader/showExchangeR.do?transactionId=${row.id}" code="transaction.show"/>&nbsp
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

	</display:table>
</security:authorize>