<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('READER')">

    <acme:showtext fieldset="true" code="book.title" value="${book.title}"/>
    <acme:showtext fieldset="true" code="book.author" value="${book.author}"/>
    <acme:showtext fieldset="true" code="book.publisher" value="${book.publisher}"/>
    <acme:showtext fieldset="true" code="book.language" value="${book.language}"/>
    <acme:showtext fieldset="true" code="book.description" value="${book.description}"/>
    <acme:showtext fieldset="true" code="book.pageNumber" value="${book.pageNumber}"/>

    <jstl:if test="${lang == 'en'}">
        <acme:showtext fieldset="true" code="book.status" value="${book.status}"/>
    </jstl:if>

    <jstl:if test="${lang == 'es'}">
        <fieldset><legend><b> <spring:message code="book.categories"/> </b></legend>
            <jstl:choose>
                <jstl:when test="${book.status == 'VERY GOOD'}">
                    <spring:message code="book.status.veryGood"/>
                </jstl:when>

                <jstl:when test="${book.status == 'GOOD'}">
                    <spring:message code="book.status.good"/>
                </jstl:when>

                <jstl:when test="${book.status == 'BAD'}">
                    <spring:message code="book.status.bad"/>
                </jstl:when>

                <jstl:when test="${book.status == 'VERY BAD'}">
                    <spring:message code="book.status.veryBad"/>
                </jstl:when>
            </jstl:choose>
        </fieldset>
    </jstl:if>

    <acme:showtext fieldset="true" code="book.isbn" value="${book.isbn}"/>
    <acme:showtext fieldset="true" code="book.moment" value="${book.moment}"/>
    <fieldset><legend><b> <spring:message code="book.photo"/></b></legend><br>
        <img src="${book.photo}" height="300" width="200"/>
    </fieldset>
   <fieldset><legend><b> <spring:message code="book.categories"/> </b></legend>
       <jstl:if test="${lang=='en' }">
           <jstl:forEach var="x" items="${categories}">
               <jstl:out value="${x.nameEn}"/>
           </jstl:forEach>
       </jstl:if>

       <jstl:if test="${lang=='es' }">
           <jstl:forEach var="x" items="${categories}">
               <jstl:out value="${x.nameEs}"/>
           </jstl:forEach>
       </jstl:if>
   </fieldset>

    <acme:cancel code="book.back" url="/book/reader/list.do"/>
</security:authorize>