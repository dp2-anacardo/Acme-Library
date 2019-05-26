
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMIN')">

<acme:showtext code="actor.name" value="${administrator.name}" fieldset="true"/>
<br>
<acme:showtext code="actor.surname" value="${administrator.surname}" fieldset="true"/>
<br>
<acme:showtext code="actor.email" value="${administrator.email}" fieldset="true"/>
<br>
<acme:showtext code="actor.phoneNumber" value="${administrator.phoneNumber}" fieldset="true"/>
<br>
<acme:showtext code="actor.address" value="${administrator.address}" fieldset="true"/>
<br>

<input type="button" name="Edit PD" value="<spring:message code="edit.PD" />"
			onclick="javascript: relativeRedir('/administrator/administrator/edit.do');" />
<input type="button" name="socialProfiles" value="<spring:message code="socialProfile" />"
			onclick="javascript: relativeRedir('/socialProfile/administrator,organizer,sponsor,reader/list.do');" />
	<input type="button" name="socialProfiles" value="<spring:message code="profile.export" />"
		   onclick="javascript: relativeRedir('/profile/exportJSON.do');" />
			
</security:authorize>
	
<security:authorize access="hasRole('REFEREE')">

<acme:showtext code="actor.name" value="${referee.name}" fieldset="true"/>
<br>
<acme:showtext code="actor.surname" value="${referee.surname}" fieldset="true"/>
<br>
<acme:showtext code="actor.email" value="${referee.email}" fieldset="true"/>
<br>
<acme:showtext code="actor.phoneNumber" value="${referee.phoneNumber}" fieldset="true"/>
<br>
<acme:showtext code="actor.address" value="${referee.address}" fieldset="true"/>
<br>

<input type="button" name="Edit PD" value="<spring:message code="edit.PD" />"
			onclick="javascript: relativeRedir('referee/referee/edit.do');" />
<input type="button" name="socialProfiles" value="<spring:message code="socialProfile" />"
			onclick="javascript: relativeRedir('/socialProfile/administrator,organizer,sponsor,reader/list.do');" />
	<input type="button" name="socialProfiles" value="<spring:message code="profile.export" />"
		   onclick="javascript: relativeRedir('/profile/exportJSON.do');" />
			
</security:authorize>



<security:authorize access="hasRole('SPONSOR')">

<acme:showtext code="actor.name" value="${sponsor.name}" fieldset="true"/>
<br>
<acme:showtext code="actor.surname" value="${sponsor.surname}" fieldset="true"/>
<br>
<acme:showtext code="actor.email" value="${sponsor.email}" fieldset="true"/>
<br>
<acme:showtext code="actor.phoneNumber" value="${sponsor.phoneNumber}" fieldset="true"/>
<br>
<acme:showtext code="actor.address" value="${sponsor.address}" fieldset="true"/>
<br>

<input type="button" name="Edit PD" value="<spring:message code="edit.PD" />"
			onclick="javascript: relativeRedir('/sponsor/sponsor/edit.do');" />
<input type="button" name="socialProfiles" value="<spring:message code="socialProfile" />"
			onclick="javascript: relativeRedir('/socialProfile/administrator,organizer,sponsor,reader/list.do');" />

	<input type="button" name="socialProfiles" value="<spring:message code="profile.export" />"
		   onclick="javascript: relativeRedir('/profile/exportJSON.do');" />

</security:authorize>

<security:authorize access="hasRole('READER')">

	<acme:showtext code="actor.name" value="${reader.name}" fieldset="true"/>
	<br>
	<acme:showtext code="actor.surname" value="${reader.surname}" fieldset="true"/>
	<br>
	<acme:showtext code="actor.email" value="${reader.email}" fieldset="true"/>
	<br>
	<acme:showtext code="actor.phoneNumber" value="${reader.phoneNumber}" fieldset="true"/>
	<br>
	<acme:showtext code="actor.address" value="${reader.address}" fieldset="true"/>
	<br>

	<input type="button" name="Edit PD" value="<spring:message code="edit.PD" />"
		   onclick="javascript: relativeRedir('/reader/reader/edit.do');" />
	<input type="button" name="socialProfiles" value="<spring:message code="socialProfile" />"
		   onclick="javascript: relativeRedir('/socialProfile/administrator,organizer,sponsor,reader/list.do');" />
	<input type="button" name="socialProfiles" value="<spring:message code="profile.export" />"
		   onclick="javascript: relativeRedir('/profile/exportJSON.do');" />

</security:authorize>

<security:authorize access="hasRole('ORGANIZER')">

	<acme:showtext code="actor.name" value="${organizer.name}" fieldset="true"/>
	<br>
	<acme:showtext code="actor.surname" value="${organizer.surname}" fieldset="true"/>
	<br>
	<acme:showtext code="actor.email" value="${organizer.email}" fieldset="true"/>
	<br>
	<acme:showtext code="actor.phoneNumber" value="${organizer.phoneNumber}" fieldset="true"/>
	<br>
	<acme:showtext code="actor.address" value="${organizer.address}" fieldset="true"/>
	<br>

	<input type="button" name="Edit PD" value="<spring:message code="edit.PD" />"
		   onclick="javascript: relativeRedir('/organizer/organizer/edit.do');" />
	<input type="button" name="socialProfiles" value="<spring:message code="socialProfile" />"
		   onclick="javascript: relativeRedir('/socialProfile/administrator,organizer,sponsor,reader/list.do');" />
	<input type="button" name="socialProfiles" value="<spring:message code="profile.export" />"
		   onclick="javascript: relativeRedir('/profile/exportJSON.do');" />

</security:authorize>

<acme:cancel url="/" code="messageBox.goBack"/>


<br>
<br>
<b><spring:message code="actor.deleteMSG"/>:</b>
<br>
<acme:cancel code="actor.deleteAccount" url="/profile/deleteInformation.do"/>
