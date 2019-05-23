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

<acme:showtext fieldset="true" code="transaction.seller" value="${transaction.seller.name}"/>
<jstl:if test="${transaction.isSale == true}">
<acme:showtext fieldset="true" code="transaction.price" value="${transaction.price}"/>
</jstl:if>
<fieldset><legend><b><spring:message code="transaction.book"/></b></legend>
    <b><spring:message code="book.title"/></b>: <jstl:out value="${transaction.book.title}"/> <br>
    <b><spring:message code="book.author"/></b>: <jstl:out value="${transaction.book.author}"/><br>
    <b><spring:message code="book.publisher"/></b>: <jstl:out value="${transaction.book.publisher}"/><br>
    <b><spring:message code="book.numPag"/></b>: <jstl:out value="${transaction.book.pageNumber}"/><br>
    <b><spring:message code="book.photo"/></b>: <img src="${transaction.book.photo}" height="200" width="200"/>
</fieldset>