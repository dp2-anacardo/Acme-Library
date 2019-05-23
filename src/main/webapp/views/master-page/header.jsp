<%--
 * header.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${configuration.banner}" alt="${configuration.systemName}" height="150" width="400"/></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/administrator/create.do"><spring:message code="master.page.administrator.register" /></a></li>
					<li><a href="administrator/referee/create.do"><spring:message code="master.page.administrator.registerReferee" /></a></li>
					<li><a href="administrator/management.do"><spring:message code="master.page.administrator.actorList" /></a></li>
					<li><a href="message/administrator/broadcast.do"><spring:message code="master.page.administrator.broadcast" /></a></li>
					<li><a href="category/administrator/list.do"><spring:message code="master.page.administrator.listCategory" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="configuration/administrator/show.do"><spring:message code="master.page.administrator.configuration" /></a></li>
			<li><a class="fNiv" href="administrator/dashboard.do"><spring:message code="master.page.dashboard" /></a></li>
		</security:authorize>

		<security:authorize access="hasRole('READER')">
			<li><a class="fNiv"><spring:message	code="master.page.reader" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="transaction/reader/listSales.do"><spring:message code="master.page.reader.listSales" /></a></li>
					<li><a href="transaction/reader/listBuys.do"><spring:message code="master.page.reader.listBuys" /></a></li>
					<li><a href="transaction/reader/listExchanges.do"><spring:message code="master.page.reader.listExchanges" /></a></li>
					<li><a href="complaint/reader/list.do"><spring:message code="master.page.reader.listComplaints" /></a></li>
					<li><a href="book/reader/list.do"><spring:message code="master.page.reader.listBooks" /></a></li>
					<li><a href="offer/reader/listB.do"><spring:message code="master.page.reader.offersMade" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.finder" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="finder/reader/edit.do"><spring:message code="master.page.reader.finder.edit" /></a></li>
					<li><a href="finder/reader/list.do"><spring:message code="master.page.reader.finder.list" /></a></li>
				</ul>
			</li>
		</security:authorize>

		<security:authorize access="hasRole('SPONSOR')">
			<li><a class="fNiv"><spring:message	code="master.page.sponsor" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="sponsorship/sponsor/list.do"><spring:message code="master.page.sponsorship.list" /></a></li>
				</ul>
			</li>
		</security:authorize>

		<security:authorize access="hasRole('REFEREE')">
			<li><a class="fNiv"><spring:message	code="master.page.referee" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="complaint/referee/list.do"><spring:message code="master.page.referee.listComplaints" /></a></li>
					<li><a href="complaint/referee/listUnassigned.do"><spring:message code="master.page.referee.listUnassigned"/></a></li>
					<li><a href="report/referee/list.do"><spring:message code="master.page.referee.listReports"/></a></li>
				</ul>
			</li>
		</security:authorize>

		<security:authorize access="hasRole('ORGANIZER')">
			<li><a class="fNiv"><spring:message	code="master.page.organizer" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="event/organizer/list.do"><spring:message code="master.page.organizer.listEvents" /></a></li>
				</ul>
			</li>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="transaction/listSales.do"><spring:message code="master.page.transaction.listSalesNotRegistered" /></a></li>
			<li><a class="fNiv" href="transaction/listExchanges.do"><spring:message code="master.page.transaction.listExchangesNotRegistered" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="profile/myInformation.do"><spring:message code="master.page.profile.myInformation" /></a></li>
					<li><a href="messageBox/list.do"><spring:message code="master.page.message" /> </a></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="transaction/listSales.do"><spring:message code="master.page.transaction.listSalesNotRegistered" /></a></li>
			<li><a class="fNiv" href="transaction/listExchanges.do"><spring:message code="master.page.transaction.listExchangesNotRegistered" /></a></li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

