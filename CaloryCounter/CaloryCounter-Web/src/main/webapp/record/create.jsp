<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="record.create.title" currentPage="new_record">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordActionBean" var="recordActionBean"/>
        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordActionBean" focus="" >
            <fieldset>
                <legend>
                    <f:message key="record.create.legend"/>
                </legend>
                <%@include file="form.jsp"%>
                <s:submit name="createRecord">
                    <f:message key="record.create"/>
                </s:submit>
            </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>