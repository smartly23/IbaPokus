<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="records.list.title" currentPage="my_records">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordsActionBean" var="actionBean"/>

        <h2><f:message key="records.list.title"/></h2>
        <c:choose>
            <c:when test="${not empty actionBean.uards.activityRecords}">
                <table class="basic" id="records">
                    <thead>
                        <tr>
                            <th><f:message key="record.date"/></th>
                            <th><f:message key="record.activity"/></th>
                            <th><f:message key="record.duration"/></th>
                            <th><f:message key="record.burnt_calories"/></th>
                            <th><f:message key="record.weight"/></th>
                            <th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${actionBean.uards.activityRecords}" var="activityRecord">
                            <tr>
                                <td>
                                    <f:formatDate pattern="${dateFormat}" value="${activityRecord.activityDate}" />
                                </td>
                                <td><c:out value="${activityRecord.activityName}"/></td>
                                <td><c:out value="${activityRecord.duration}"/></td>
                                <td><c:out value="${activityRecord.caloriesBurnt}"/></td>
                                <td><f:message key="weightCat${activityRecord.weightCategory.name}"/></td>
                                <td>
                                    <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordActionBean" event="edit" class="button"><s:param name="record.activityRecordId" value="${activityRecord.activityRecordId}" /><f:message key="edit"/></s:link>
                                    <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordActionBean" event="delete" class="button"><s:param name="record.activityRecordId" value="${activityRecord.activityRecordId}" /><f:message key="delete"/></s:link>
                                    </td>
                                </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p><f:message key="records.list.norecord"/></p>
            </c:otherwise>
        </c:choose>
    </s:layout-component>
</s:layout-render>