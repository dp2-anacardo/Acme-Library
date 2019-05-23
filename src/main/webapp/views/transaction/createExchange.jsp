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
    <form:form action="transaction/reader/createExchange.do" modelAttribute="transaction">

    <form:label path="book">
        <spring:message code="transaction.book"/>
    </form:label>
    <form:select path="book">
        <form:options items="${books}" itemLabel="title" itemValue="id"/>
    </form:select>
        <form:errors cssClass="error" path="book" />
    <br/>
        <acme:submit name="save" code="transaction.save"/>
        <acme:cancel code="transaction.cancel" url="/transaction/reader/listExchanges.do"/>
    </form:form>
    </security:authorize>
