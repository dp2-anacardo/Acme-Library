
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:showtext code="actor.name" value="${actor.name}" fieldset="true"/>
<br>
<acme:showtext code="actor.surname" value="${actor.surname}" fieldset="true"/>
<br>
<acme:showtext code="actor.email" value="${actor.email}" fieldset="true"/>
<br>
<acme:showtext code="actor.phoneNumber" value="${actor.phoneNumber}" fieldset="true"/>
<br>
<acme:showtext code="actor.address" value="${actor.address}" fieldset="true"/>
<br>
<acme:cancel url="/" code="messageBox.goBack"/>

