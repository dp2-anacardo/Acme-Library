<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

    <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
    <%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
    <%@taglib prefix="display" uri="http://displaytag.sf.net" %>
    <%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="complaint/reader/create.do" modelAttribute="complaint">

    <form:hidden path="id" readOnly = "true"/>
    <input type="hidden" name="transactionId" value="${transactionId}" readonly>

    <acme:textarea code="complaint.body" path="body"/>
    <br>

    <acme:textarea code="complaint.attachments" path="attachments" />
    <jstl:if test="${not empty attachmentError }">
        <p class="error">${attachmentError }</p>
    </jstl:if>
    <br/>

    <acme:submit name="save" code="complaint.save"/>

    <acme:cancel url="/" code="complaint.cancel"/>

</form:form>