<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="stats.ladder.title" currentPage="global_ladder">
    <s:layout-component name="body">
        <h2><f:message key="stats.ladder.title"/></h2>
        <table id="globalLadder" class="basic">
            <thead>
            <th>#&nbsp;</th>
            <th><f:message key="stats.username"/></th>
            <th><f:message key="stats.caloriessum"/></th>
            <th><f:message key="stats.durationsum"/></th>
        </thead>
        <tbody>
            <c:set var="count" value="0" scope="page" />
            <c:forEach items="${actionBean.usersStats}" var="userStat">
                <c:set var="count" value="${count + 1}" scope="page"/>
                <tr>
                    <td>${count}</td>
                    <td><c:out value="${userStat.nameOfUser}"/></td>
                    <td><c:out value="${userStat.sumBurntCalories}"/></td>
                    <td><c:out value="${userStat.sumDuration}"/></td>
                </c:forEach>
        </tbody>
    </table>
</s:layout-component>
</s:layout-render>