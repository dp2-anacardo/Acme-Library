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
        <display:column title="${moment}">
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
            <jstl:if test="${row.referee != null}">
            <a href="profile/show.do?actorId=${row.referee.id}">
                <spring:message code="complaint.referee"/></a>
            </jstl:if>
        </display:column>

    <spring:message code="complaint.reports" var="reports"/>
    <display:column title="${reports}">
        <a href="report/reader/list.do?complaintId=${row.id}">
            <spring:message code="complaint.reports"/></a>
    </display:column>

        <spring:message code="complaint.show" var="show"/>
        <display:column title="${show}">
            <a href="complaint/reader/show.do?complaintId=${row.id}">
                <spring:message code="complaint.show"/></a>
        </display:column>

            <acme:cancel url="/" code="complaint.back"/>
</security:authorize>

<security:authorize access="hasRole('REFEREE')">
        <spring:message code="complaint.reader" var="reader"/>
        <display:column title="${reader}">
            <a href="profile/show.do?actorId=${row.reader.id}">
                <spring:message code="complaint.reader"/></a>
        </display:column>

    <jstl:if test="${!b}">
         <spring:message code="complaint.show" var="show"/>
         <display:column title="${show}">
             <a href="complaint/referee/show.do?complaintId=${row.id}">
                 <spring:message code="complaint.show"/></a>
         </display:column>

        <spring:message code="report.create" var="report"/>
        <display:column title="${report}">
            <a href="report/referee/create.do?complaintId=${row.id}">
                <spring:message code="report.create"/></a>
        </display:column>
    </jstl:if>

    <jstl:if test="${b}">
        <spring:message code="complaint.autoAssign" var="autoAssign"/>
        <display:column title="${autoAssign}">
            <a href="complaint/referee/autoAssign.do?complaintId=${row.id}">
                <spring:message code="complaint.autoAssign"/></a>
        </display:column>
    </jstl:if>

        <div>
            <acme:cancel url="/" code="complaint.back"/>
        </div>
</security:authorize>

    </display:table>