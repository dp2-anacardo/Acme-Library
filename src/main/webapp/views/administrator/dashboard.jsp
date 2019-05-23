<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
    <%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
    <%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <%@taglib prefix="security"
              uri="http://www.springframework.org/security/tags"%>
    <%@taglib prefix="display" uri="http://displaytag.sf.net"%>
</head>
<body>

<security:authorize access="hasRole('ADMIN')">

    <b><spring:message code="administrator.Top5OrganizersWithMoreEvents" /></b>
    <jstl:forEach var="x" items="${Top5OrganizersWithMoreEvents}">
        <br>
        - ${x.name}
    </jstl:forEach>
    <br>

    <b><spring:message code="administrator.AvgTransactionsPrice"/></b> ${AvgTransactionsPrice}<br/>
    <b><spring:message code="administrator.MinTransactionsPrice"/></b> ${MinTransactionsPrice}<br/>
    <b><spring:message code="administrator.MaxTransactionsPrice"/></b> ${MaxTransactionsPrice}<br/>
    <b><spring:message code="administrator.StddevTransactionsPrice"/></b> ${StddevTransactionsPrice}<br/>

    <b><spring:message code="administrator.AvgBooksPerReader"/></b> ${AvgBooksPerReader}<br/>
    <b><spring:message code="administrator.MinBooksPerReader"/></b> ${MinBooksPerReader}<br/>
    <b><spring:message code="administrator.MaxBooksPerReader"/></b> ${MaxBooksPerReader}<br/>
    <b><spring:message code="administrator.StddevBooksPerReader"/></b> ${StddevBooksPerReader}<br/>

    <b><spring:message code="administrator.AvgTransactionsComplaint"/></b> ${AvgTransactionsComplaint}<br/>
    <b><spring:message code="administrator.MinTransactionsComplaint"/></b> ${MinTransactionsComplaint}<br/>
    <b><spring:message code="administrator.MaxTransactionsComplaint"/></b> ${MaxTransactionsComplaint}<br/>
    <b><spring:message code="administrator.StddevTransactionsComplaint"/></b> ${StddevTransactionsComplaint}<br/>

    <b><spring:message code="administrator.AvgSponsorPerEvent"/></b> ${AvgSponsorPerEvent}<br/>
    <b><spring:message code="administrator.MinSponsorPerEvent"/></b> ${MinSponsorPerEvent}<br/>
    <b><spring:message code="administrator.MaxSponsorPerEvent"/></b> ${MaxSponsorPerEvent}<br/>
    <b><spring:message code="administrator.StddevSponsorPerEvent"/></b> ${StddevSponsorPerEvent}<br/>

    <b><spring:message code="administrator.RatioOfActiveVSInnactiveSpons"/></b> ${RatioOfActiveVSInnactiveSpons}<br/>

    <b><spring:message code="administrator.RatioOfFullFinders"/></b> ${RatioOfFullFinders}<br/>

    <b><spring:message code="administrator.RatioOfEmptyFinders"/></b> ${RatioOfEmptyFinders}<br/>

    <b><spring:message code="administrator.RatioOfEmptyVSFullFinders"/></b> ${RatioOfEmptyVSFullFinders}<br/>

    <b><spring:message code="administrator.RatioOfEmptyVSFullTransactionsComplaints"/></b> ${RatioOfEmptyVSFullTransactionsComplaints}<br/>

    <b><spring:message code="administrator.Top5ReadersWithMoreComplaints"/></b>
    <jstl:forEach var="x" items="${Top5ReadersWithMoreComplaints}">
        <br>
        - ${x.name}
    </jstl:forEach>
    <br>

    <b><spring:message code="administrator.RatioOfSalesVSExchanges"/></b> ${RatioOfSalesVSExchanges}<br/>

</security:authorize>
</body>
</html>