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

<form:form action="comment/create.do" modelAttribute="comment">

    <form:hidden path="id" readOnly = "true"/>
    <input type="hidden" name="reportId" value="${reportId}" readonly>
    <br>

    <acme:textbox code="comment.body" path="body"/>
    <form:errors cssClass="error" path="body" />

    <acme:submit name="save" code="comment.save"/>

    <security:authorize access="hasRole('READER')">
        <div>
            <acme:cancel url="report/reader/list.do" code="comment.cancel"/>
        </div>
    </security:authorize>

    <security:authorize access="hasRole('REFEREE')">
        <div>
            <acme:cancel url="report/referee/list.do" code="comment.cancel"/>
        </div>
    </security:authorize>

</form:form>