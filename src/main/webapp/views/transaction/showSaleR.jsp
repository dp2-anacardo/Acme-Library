<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('READER')">
<acme:showtext fieldset="true" code="transaction.seller" value="${t.seller.name}"/>
<acme:showtext fieldset="true" code="transaction.buyer" value="${t.buyer.name}"/>
<acme:showtext fieldset="true" code="transaction.price" value="${t.price}"/>
<fieldset><legend><b><spring:message code="transaction.book"/></b></legend>
    <b><spring:message code="book.title"/></b>: <jstl:out value="${t.book.title}"/> <br>
    <b><spring:message code="book.author"/></b>: <jstl:out value="${t.book.author}"/><br>
    <b><spring:message code="book.publisher"/></b>: <jstl:out value="${t.book.publisher}"/><br>
    <b><spring:message code="book.numPag"/></b>: <jstl:out value="${t.book.pageNumber}"/><br>
    <b><spring:message code="book.photo"/></b>: <img src="${t.book.photo}" height="300" width="200"/>
</fieldset>
</security:authorize>