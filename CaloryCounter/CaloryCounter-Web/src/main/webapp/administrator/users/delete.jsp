<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<s:layout-render name="/layout.jsp" titlekey="user.delete">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.UsersAdministrationActionBean" var="actionBean"/>

        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.UsersAdministrationActionBean">
            <s:hidden name="user.activityId"/>
            <s:hidden name="activity.activityName"/>
            <fieldset>
                <legend>
                    <f:message key="user.delete.areyousure"/>
                </legend>
                <s:errors/>
                <s:hidden name="user.userId" value="${actionBean.user.userId}"/>
                <table>
                    <tr>
                        <th><s:label for="username" name="user.username"/></th>
                        <td><s:text id="username" name="user.username" disabled="true"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="name" name="user.name"/></th>
                        <td><s:text id="name" name="user.name" disabled="true"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="sex" name="user.sex"/></th>
                        <td><s:text id="sex" name="user.sex" disabled="true"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="age" name="user.age"/></th>
                        <td><s:text id="age" name="user.age" disabled="true"/></td>
                    </tr>
                </table>
                <div>
                    <s:submit name="confirmDelete">
                        <f:message key="delete"/>
                    </s:submit>
                    <s:submit name="cancelOperation">
                        <f:message key="cancel"/>
                    </s:submit>
                </div>
            </fieldset>
        </s:form>

    </s:layout-component>
</s:layout-render>