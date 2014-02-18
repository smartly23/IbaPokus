<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<s:layout-render name="/layout.jsp" titlekey="activity.edit">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" var="actionBean"/>

        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean">
            <s:hidden name="activity.activityId"/>
            <fieldset>
                <legend>
                    <f:message key="activity.edit"/>
                </legend>
                <%@include file="form.jsp"%>
                <div>
                    <s:submit name="confirmEdit">
                        <f:message key="saveChanges"/>
                    </s:submit>
                    <s:submit name="cancelOperation">
                        <f:message key="cancel"/>
                    </s:submit>
                </div>
            </fieldset>
        </s:form>

    </s:layout-component>
</s:layout-render>