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
<%@ taglib prefix="for" uri="http://java.sun.com/jsp/jstl/core" %>

<security:authorize access="hasRole('READER')">
    <display:table name="events" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

        <spring:message code="register.event.title" var="columTitle"/>
        <display:column title="${columTitle}">
            <jstl:out value="${row.title}"/>
        </display:column>

        <spring:message code="register.date" var="columTitle"/>
        <display:column title="${columTitle}">
            <jstl:forEach var="x" items="${row.registers}">
                <jstl:if test="${x.reader == reader}">
                    <jstl:out value="${x.moment}"/>
                    <jstl:set var="regId" value="${x.id}" />
                </jstl:if>
            </jstl:forEach>
        </display:column>

        <display:column >
            <acme:cancel code="register.delete" url="register/reader/delete.do?registerId=${regId}&eventId=${row.id}"/>
        </display:column>

      </display:table>
  </security:authorize>