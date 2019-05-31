<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('READER')">

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.title" var="title"/>
        <display:column property="title" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.author" var="title"/>
        <display:column property="author" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.publisher" var="title"/>
        <display:column property="publisher" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.language" var="title"/>
        <display:column property="language" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.description" var="title"/>
        <display:column property="description" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.pageNumber" var="title"/>
        <display:column property="pageNumber" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.status" var="title"/>
        <display:column property="status" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.isbn" var="title"/>
        <display:column property="isbn" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.moment" var="title"/>
        <display:column property="moment" title="${title}"
                        sortable="false"/>
    </display:table>

    <display:table name="book" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.photo" var="title"/>
        <display:column title="${title}" sortable="false">
            <img src="${row.photo}" height="300" width="200"/>
        </display:column>
    </display:table>

    <display:table name="categories" id="row" requestURI="book/show.do"
                   class="displaytag">
        <spring:message code="book.categories" var="title"/>
        <display:column title="${title}" sortable="false">
            <jstl:if test="${lang=='en' }">
                <jstl:out value="${row.nameEn}"/>
            </jstl:if>

            <jstl:if test="${lang=='es' }">
                <jstl:out value="${row.nameEs}"/>
            </jstl:if>
        </display:column>
    </display:table>

    <acme:cancel code="book.back" url="/book/reader/list.do"/>
</security:authorize>