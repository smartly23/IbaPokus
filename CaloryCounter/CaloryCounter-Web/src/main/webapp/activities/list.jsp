<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="activities.list.title" pagename="activities" currentPage="activities">
    <s:layout-component name="body">
        <h2><f:message key="activities.list.title"/></h2>
        <c:set var="tableId" value="activities"/>
        <c:if test="${sessionScope.user!=null && sessionScope.user.role==actionBean.adminRole}">
            <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesActionBean" action="switchView" >
                <s:select name="showDeleted">
                    <s:option value="false" ><f:message key="activities.active"/></s:option>
                    <s:option value="true"><f:message key="activities.deleted"/></s:option>
                </s:select>
                <s:submit name="switchView"><f:message key="activities.switchView"/></s:submit>
            </s:form>
            <c:set var="tableId" value="activitiesAdmin"/>
        </c:if>

        <table id="${tableId}" class="basic">
            <thead>
                <tr>
                    <th rowspan="2" class="col1"><f:message key="activity.name"/></th>
                    <th colspan="4"><f:message key="activity.calories"/></th>
                        <c:if test="${sessionScope.user!=null && sessionScope.user.role==actionBean.adminRole}">
                        <th rowspan="2">&nbsp;</th>
                        </c:if>
                </tr>
                <tr>
                    <c:forEach items="${actionBean.weightCategories}" var="category">
                        <th><f:message key="weightCat${category.name}"/></th>
                        </c:forEach>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${actionBean.activities}" var="activity">
                    <tr>
                        <td class="col1"><c:out value="${activity.activityName}"/></td>
                        <c:forEach items="${activity.weightCalories}" var="weightCalories">
                            <td><c:out value="${weightCalories.value}"/></td>
                        </c:forEach>
                        <c:if test="${sessionScope.user!=null && sessionScope.user.role==actionBean.adminRole}">
                            <td>
                                <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" event="edit" class="button"><s:param name="activity.activityId" value="${activity.activityId}" /><f:message key="edit"/></s:link>
                                <c:choose>
                                    <c:when test="${actionBean.showDeleted==true}">
                                        <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" event="restore" class="button"><s:param name="activity.activityId" value="${activity.activityId}" /><f:message key="restore"/></s:link>
                                    </c:when>
                                    <c:otherwise>
                                        <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" event="delete" class="button"><s:param name="activity.activityId" value="${activity.activityId}" /><f:message key="delete"/></s:link>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <c:if test="${sessionScope.user!=null && sessionScope.user.role==actionBean.adminRole}">
            <br />
            <p>
                <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" event="create" class="button"><f:message key="activity.create"/></s:link><br />
                <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" event="updateFromPage" class="button"><f:message key="activities.update"/></s:link>
                </p>
        </c:if>
    </s:layout-component>
</s:layout-render>