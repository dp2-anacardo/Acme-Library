
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
<form:form action="category/administrator/edit.do" modelAttribute="category">


        <form:input type ="hidden" path="id" readonly="true"/>

        <acme:textbox code="category.nameEN" path="nameEn" />

        <acme:textbox code="category.nameES" path="nameEs" />


        <acme:submit name="save" code="category.save"/>
        <jstl:if test="${category.id != 0}">
             <acme:cancel code="category.delete" url="category/administrator/delete.do?categoryId=${category.id}"/>
        </jstl:if>

        <acme:cancel code="category.back" url="/category/administrator/list.do"/>

</form:form>
</security:authorize>