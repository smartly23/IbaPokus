<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="activities.update">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" var="actionBean"/>
        <s:errors/>
        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean">
            <fieldset><legend><f:message key="activities.update.areyousure"/></legend>
                <p><f:message key="activities.update.areyousure.message"/> </p>
                <p style="padding:20px">
                    <s:checkbox id="i1" name="removeDeprecated" checked="false"/> <f:message key="activities.update.removeDecprecated"/>
                </p>

                <s:submit name="updateActivities"><f:message key="yes"/></s:submit>
                <s:submit name="cancelOperationActivity"><f:message key="no"/></s:submit>
                </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>
