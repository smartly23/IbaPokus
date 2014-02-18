<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<s:layout-render name="/layout.jsp" titlekey="record.edit.title">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordActionBean" var="actionBean"/>

        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordActionBean">
            <s:hidden name="record.activityRecordId"/>
            <s:hidden id="activityName" name="record.activityName"/>
            <fieldset>
                <legend>
                    <f:message key="record.edit.legend"/>
                </legend>
                <%@include file="form.jsp"%>
                <div>
                    <s:submit name="save">
                        <f:message key="record.edit.save"/>
                    </s:submit>
                    <s:submit name="cancel">
                        <f:message key="record.edit.cancel"/>
                    </s:submit>
                </div>
            </fieldset>
        </s:form>

    </s:layout-component>
</s:layout-render>