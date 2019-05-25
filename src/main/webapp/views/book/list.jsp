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

<security:authorize access="hasRole('READER')">
    <display:table name="books" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

        <spring:message code="book.title" var="columTitle"/>
        <display:column title="${columTitle}">
            <jstl:out value="${row.title}"/>
        </display:column>

        <spring:message code="book.author" var="columTitle"/>
        <display:column title="${columTitle}">
            <jstl:out value="${row.author}"/>
        </display:column>

        <spring:message code="book.isbn" var="columTitle"/>
        <display:column title="${columTitle}">
            <jstl:out value="${row.isbn}"/>
        </display:column>

        <spring:message code="book.show" var="showTitle"/>
        <display:column title="${showTitle}">
            <a href="book/reader/show.do?bookId=${row.id}">
                <spring:message code="book.show"/>
            </a>
        </display:column>

        <spring:message code="book.edit" var="editTitle"/>
        <display:column title="${editTitle}">
            <a href="book/reader/edit.do?bookId=${row.id}">
                <spring:message code="book.edit"/>
            </a>
        </display:column>
    </display:table>

    <input type="button" value="<spring:message code="book.create" />"
           onclick="javascript: relativeRedir('book/reader/create.do');" />
</security:authorize>