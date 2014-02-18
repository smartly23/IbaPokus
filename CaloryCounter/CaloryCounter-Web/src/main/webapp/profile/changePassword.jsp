<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="profile.changePassword">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.ProfileActionBean" var="actionBean"/>
        <s:errors/>
        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.ProfileActionBean">
            <s:hidden name="username"/>
            <fieldset><legend><f:message key="profile.changePassword"/></legend>
                <table>
                    <tr>
                        <th><s:label for="oldPassword" name="profile.oldPassword"/></th>
                        <td><s:password id="oldPassword" name="oldPassword"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="newPassword" name="profile.newPassword"/></th>
                        <td><s:password id="newPassword" name="newPassword"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="confirmPassword" name="profile.repeatPassword"/></th>
                        <td><s:password id="confirmPassword" name="confirmNewPassword"/></td>
                    </tr>
                </table>
                <s:submit name="confirmChangePassword"><f:message key="profile.saveNewPassword"/></s:submit>
                <s:submit name="cancel"><f:message key="profile.cancel"/></s:submit>
                </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>