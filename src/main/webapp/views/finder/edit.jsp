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
    <form:form action ="finder/reader/edit.do" modelAttribute="finder">

        <form:hidden path="id" />

        <!-- Single Attributes -->
        <jstl:out value="${messageCode}"/>

        <acme:textbox code="finder.update.keyword" path="keyWord"/>

        <form:label path="categoryName">
            <spring:message code="finder.update.category" />:
        </form:label>
        <form:select path="categoryName">
            <form:option value="" label="----" />
            <form:options items="${cNames}"
            />
        </form:select>
        <form:errors cssClass="error" path="categoryName" />
        <br />

        <form:label path="status">
            <spring:message code="finder.update.status" />:
        </form:label>
        <form:select path="status">
            <form:option value="" label="----" />
            <form:options items="${status}"
            />
        </form:select>
        <form:errors cssClass="error" path="status" />
        <br />

        <!-- Submit and Cancel -->

        <acme:submit name="save" code="finder.update.update"/>&nbsp

        <acme:cancel url="/" code="finder.update.cancel"/>&nbsp

        <acme:cancel url="finder/rookie/clear.do" code="finder.update.clear"/>&nbsp

    </form:form>
</security:authorize>
