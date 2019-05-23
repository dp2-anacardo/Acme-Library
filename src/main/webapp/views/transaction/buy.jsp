<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
    <%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
    <%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
    <%@taglib prefix="display" uri="http://displaytag.sf.net"%>
    <%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

    <security:authorize access="hasRole('READER')">
    <form:form action="transaction/reader/buy.do" modelAttribute="transaction">
        <form:hidden path="id" />
        <acme:textbox code="credit.holderName" path="creditCard.holder"/>
    <br/>

    <form:label path="creditCard.brandName">
        <spring:message code="sponsorship.creditCard.brandName" />:
    </form:label>
    <form:select id="brandName" path="creditCard.brandName">
        <form:options items="${brandList}"/>
    </form:select>
        <form:errors class="error" path="creditCard.brandName" />
    <br/>
    <br/>

        <acme:textbox code="credit.number" path="creditCard.number"/>
    <br/>

        <acme:textbox code="credit.expiration" path="creditCard.expirationYear" placeholder="MM/YY"/>
    <br/>

        <acme:textbox code="credit.cvvCode" path="creditCard.cvv"/>
        <acme:submit name="save" code="transaction.buy"/>
        <acme:cancel code="transaction.cancel" url="/"/>
    </form:form>

    </security:authorize>
