<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="profile.login">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.AuthenticationActionBean" var="actionBean"/>
        <s:errors/>
        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.AuthenticationActionBean">
            <fieldset><legend><f:message key="profile.login"/></legend>
                <table>
                    <tr>
                        <th><s:label for="username" name="profile.username"/></th>
                        <td><s:text id="username" name="username"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="password" name="profile.password"/></th>
                        <td><s:password id="password" name="password"/></td>
                    </tr>
                </table>
                <s:submit name="login"><f:message key="profile.login"/></s:submit>
                <s:submit name="cancel"><f:message key="profile.cancel"/></s:submit>
                </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>
