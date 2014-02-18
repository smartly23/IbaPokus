<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="activities.update">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesAdministrationActionBean" var="actionBean"/>
        <s:errors/>
        <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesActionBean" class="button"><f:message key="ok"/></s:link>
    </s:layout-component>
</s:layout-render>