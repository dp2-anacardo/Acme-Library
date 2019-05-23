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

    <display:table name="reports" id="row" requestURI="${requestURI}"
                   pagesize="5" class="displaytag">

        <spring:message code="report.moment" var="moment"/>
        <display:column title="${moment}">
            <jstl:out value="${row.moment}"/>
        </display:column>

        <spring:message code="report.description" var="description"/>
        <display:column title="${description}">
            <jstl:out value="${row.description}"/>
        </display:column>

        <spring:message code="report.attachments" var="attachments"/>
        <display:column title="${attachments}">
            <jstl:forEach items="${row.attachments}" var="url">
                <jstl:out value="${url.link}"></jstl:out>
            </jstl:forEach>
        </display:column>

<security:authorize access="hasRole('REFEREE')">
        <spring:message code="report.isFinal" var="isFinal"/>
        <display:column title="${isFinal}">
            <jstl:if test="${row.isFinal eq true}">
                <spring:message code="report.final"/>
            </jstl:if>
            <jstl:if test="${row.isFinal eq false}">
                <spring:message code="report.draft"/>
            </jstl:if>
        </display:column>
</security:authorize>

        <spring:message code="report.comments" var="comments"/>
        <display:column title="${comments}">
            <a href="comment/list.do?reportId=${row.id}">
                <spring:message code="report.comments"/></a>
        </display:column>

<security:authorize access="hasRole('READER')">
        <spring:message code="report.complaint" var="complaint"/>
        <display:column title="${complaint}">
            <a href="complaint/reader/show.do?complaintId=${row.complaint.id}">
                <spring:message code="report.complaint"/></a>
        </display:column>

    <spring:message code="comment.create" var="comment"/>
    <display:column title="${comment}">
        <a href="comment/create.do?reportId=${row.id}">
            <spring:message code="comment.create"/></a>
    </display:column>

        <div>
            <acme:cancel url="complaint/reader/list.do" code="report.back"/>
        </div>
</security:authorize>

<security:authorize access="hasRole('REFEREE')">
        <spring:message code="report.complaint" var="complaint"/>
        <display:column title="${complaint}">
            <a href="complaint/referee/show.do?complaintId=${row.complaint.id}">
                <spring:message code="report.complaint"/></a>
        </display:column>

         <spring:message code="report.edit" var="edit"/>
         <display:column title="${edit}">
             <jstl:if test="${!row.isFinal}">
             <a href="report/referee/edit.do?reportId=${row.id}">
                 <spring:message code="report.edit"/></a>
             </jstl:if>
         </display:column>

    <spring:message code="comment.create" var="comment"/>
    <display:column title="${comment}">
        <jstl:if test="${row.isFinal}">
            <a href="comment/create.do?reportId=${row.id}">
                <spring:message code="comment.create"/></a>
        </jstl:if>
    </display:column>

            <acme:cancel url="complaint/referee/list.do" code="report.back"/>

</security:authorize>

    </display:table>