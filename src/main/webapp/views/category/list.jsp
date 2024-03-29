
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

<security:authorize access="hasRole('ADMIN')">
    <display:table name="categories" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">
        <spring:message code="category.name" var="columnTitle"/>
        <jstl:if test="${lang=='es' }">
            <display:column title="${columnTitle}">
                <jstl:out value="${row.nameEs}"/>
            </display:column>
        </jstl:if>

        <jstl:if test="${lang=='en' }">
            <display:column title="${columnTitle}">
                <jstl:out value="${row.nameEn}"/>
            </display:column>
        </jstl:if>

        <spring:message code="category.view" var="columnTitle"/>
        <display:column title="${columnTitle}">
            <a href="category/administrator/show.do?categoryId=${row.id}">
                <spring:message code="category.view"/>
            </a>
        </display:column>

        <spring:message code="category.edit" var="columnTitle"/>
        <display:column title="${columnTitle}">
            <jstl:if test="${row.nameEn != 'Default'}">
            <a href="category/administrator/edit.do?categoryId=${row.id}">
                <spring:message code="category.edit"/>
            </a>
            </jstl:if>
        </display:column>

        <spring:message code="category.delete" var="columnTitle"/>
        <display:column title="${columnTitle}">
            <jstl:if test="${category.id != 0 and row.nameEn != 'Default'}">
                <acme:cancel code="category.delete" url="category/administrator/delete.do?categoryId=${row.id}"/>
            </jstl:if>
        </display:column>
    </display:table>

    <input type="button" value="<spring:message code="category.create" />"
           onclick="javascript: relativeRedir('category/administrator/create.do');" />
</security:authorize>