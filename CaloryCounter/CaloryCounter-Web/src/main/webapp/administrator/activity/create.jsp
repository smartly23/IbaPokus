<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="activity.create">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" var="actionBean"/>
        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" focus="" >
            <fieldset>
                <legend>
                    <f:message key="activity.create"/>
                </legend>
                <%@include file="form.jsp"%>
                <s:submit name="confirmCreate">
                    <f:message key="activity.create"/>
                </s:submit>
            </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>