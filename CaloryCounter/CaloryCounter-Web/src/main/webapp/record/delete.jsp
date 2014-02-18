<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="record.delete.title">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordActionBean" var="recordActionBean"/>
        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordActionBean" focus="" >
            <fieldset>
                <legend>
                    <f:message key="record.delete.question"/>
                </legend>
                <s:hidden name="record.activityRecordId"/>
                <s:hidden name="record.userId" value="${actionBean.user.userId}"/>
                <s:hidden name="record.weightCategory" value="${actionBean.user.weightCategory}"/>
                <table>
                    <tr>
                        <th><s:label for="activityName" name="record.activity"/></th>
                        <td><s:text id="activityName" name="record.activityName" disabled="true"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="duration" name="record.duration"/></th>
                        <td><s:text id="duration" name="record.duration" size="4" disabled="true"/> <f:message key="minutes"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="date" name="record.date"/></th>
                        <td><s:text id="date" name="record.activityDate" disabled="true"/></td>
                    </tr>
                </table>
                <s:submit name="confirmDelete">
                    <f:message key="record.delete.confirm"/>
                </s:submit>
                <s:submit name="cancel">
                    <f:message key="record.delete.cancel"/>
                </s:submit>
            </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>