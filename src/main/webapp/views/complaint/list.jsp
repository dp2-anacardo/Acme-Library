<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

    <display:table name="complaints" id="row" requestURI="${requestURI}"
                   pagesize="5" class="displaytag">

        <spring:message code="complaint.moment" var="moment"/>
        <display:column title="${moment}" sortable="true">
            <jstl:out value="${row.moment}"/>
        </display:column>

        <spring:message code="complaint.body" var="body"/>
        <display:column title="${body}">
            <jstl:out value="${row.body}"/>
        </display:column>

        <spring:message code="complaint.attachments" var="attachments"/>
        <display:column title="${attachments}">
            <jstl:forEach items="${row.attachments}" var="url">
                <jstl:out value="${url.link}"></jstl:out>
            </jstl:forEach>
        </display:column>

<security:authorize access="hasRole('READER')">
        <spring:message code="complaint.referee" var="referee"/>
        <display:column title="${referee}">
            <a href="profile/show.do?actorId=${row.referee.id}">
                <spring:message code="complaint.referee"/></a>
        </display:column>

        <div>
            <acme:cancel url="/" code="complaint.back"/>
        </div>
</security:authorize>

<security:authorize access="hasRole('REFEREE')">
        <spring:message code="complaint.reader" var="reader"/>
        <display:column title="${reader}">
            <a href="profile/show.do?actorId=${row.reader.id}">
                <spring:message code="complaint.reader"/></a>
        </display:column>

        <div>
            <acme:cancel url="/" code="complaint.back"/>
        </div>
</security:authorize>

    </display:table>