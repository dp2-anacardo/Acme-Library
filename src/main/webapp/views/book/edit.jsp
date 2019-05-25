<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('READER')">
    <form:form action="book/reader/edit.do" modelAttribute="book">

        <form:input type ="hidden" path="id" readonly="true"/>

        <acme:textbox code="book.title" path="title" />

        <acme:textbox code="book.author" path="author" />

        <acme:textbox code="book.publisher" path="publisher" />

        <acme:textbox code="book.language" path="language" />

        <acme:textarea code="book.description" path="description" />

        <acme:textbox code="book.pageNumber" path="pageNumber" />

        <%--
        <acme:textbox code="book.status" path="status" placeholder="VERY GOOD, GOOD, BAD or VERY BAD" />
        --%>

        <spring:message code="book.status"/>
        <form:select path="status" multiple="false">
            <form:option value="VERY GOOD"><spring:message code="book.status.veryGood"/></form:option>
            <form:option value="GOOD"><spring:message code="book.status.good"/></form:option>
            <form:option value="BAD"><spring:message code="book.status.bad"/></form:option>
            <form:option value="VERY BAD"><spring:message code="book.status.veryBad"/></form:option>
        </form:select>
        <form:errors class="error" path="status"/>
        <br>

        <acme:textbox code="book.isbn" path="isbn"/>

        <acme:textbox code="book.photo" path="photo" />

        <form:label path="categories">
            <spring:message code="book.categories"/>
        </form:label>
        <form:select path="categories">
            <jstl:if test="${pageContext.response.locale.language == 'es'}">
                <form:options items="${categories}" itemValue="id" itemLabel="nameEs"/>
            </jstl:if>
            <jstl:if test="${pageContext.response.locale.language == 'en'}">
                <form:options items="${categories}" itemValue="id" itemLabel="nameEn"/>
            </jstl:if>
        </form:select>
        <br/>

        <acme:submit name="save" code="book.save"/>
        <jstl:if test="${book.id != 0}">
            <acme:cancel code="book.delete" url="book/reader/delete.do?bookId=${book.id}"/>
        </jstl:if>

        <acme:cancel code="book.back" url="/book/reader/list.do"/>



    </form:form>
</security:authorize>