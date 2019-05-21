<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
    <%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@taglib prefix="security"
              uri="http://www.springframework.org/security/tags" %>
    <%@taglib prefix="display" uri="http://displaytag.sf.net" %>
    <%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

</head>
<body>
<form:form action="sponsorship/sponsor/update.do" modelAttribute="sponsorship">

    <%--  Hidden properties --%>
    <form:hidden path="id"/>

    <%-- Banner --%>
    <acme:textbox code="sponsorship.banner" path="banner"/>
    <br>

    <%-- Status --%>
    <spring:message code="sponsorship.status"/>
    <form:select path="status" multiple="false">
        <form:option value="1"><spring:message code="sponsorship.status.on"/></form:option>
        <form:option value="0"><spring:message code="sponsorship.status.off"/></form:option>
    </form:select>
    <form:errors class="error" path="status"/>
    <br>

    <%-- CreditCard --%>
    <fieldset>
        <legend><spring:message code="actor.CreditCard" /></legend>

        <acme:textbox code="credit.holderName" path="creditCard.holder"/>
        <br />

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
        <br />

        <acme:textbox code="credit.expiration" path="creditCard.expirationYear"/>
        <br />

        <acme:textbox code="credit.cvvCode" path="creditCard.cvv"/>
        <br />
    </fieldset>

    <%-- Buttons --%>
    <security:authorize access="hasRole('SPONSOR')">
        <acme:submit name="update" code="sponsorship.save"/>

        <acme:cancel url="sponsorship/sponsor/list.do" code="sponsorship.back"/>

    </security:authorize>

</form:form>
</body>
</html>