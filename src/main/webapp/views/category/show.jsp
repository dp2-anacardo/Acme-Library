<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMIN')">
<jstl:if test="${lang=='en' }">
    <display:table name="category" id="row" requestURI="category/show.do"
               class="displaytag">
        <spring:message code="category.name" var="nameEn"/>
        <display:column property="nameEn" title="${nameEn}"
                    sortable="false"/>
    </display:table>
</jstl:if>

<jstl:if test="${lang=='es' }">
    <display:table name="category" id="row" requestURI="category/show.do"
                   class="displaytag">
        <spring:message code="category.name" var="nameEs"/>
        <display:column property="nameEs" title="${nameEs}"
                        sortable="false"/>
    </display:table>
</jstl:if>

<acme:cancel code="category.back" url="/category/administrator/list.do"/>
</security:authorize>