<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="profile.edit">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.AuthenticationActionBean" var="actionBean"/>
        <s:errors/>
        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.AuthenticationActionBean">
            <fieldset><legend><f:message key="profile.register"/></legend>
                <table>
                    <tr>
                        <th><s:label for="i1" name="profile.username"/></th>
                        <td><s:text id="i1" name="user.username"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="i2" name="profile.password"/></th>
                        <td><s:password id="i2" name="password"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="i3" name="profile.name"/></th>
                        <td><s:text id="i3" name="user.name"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="i4" name="profile.age"/></th>
                        <td><s:text id="i4" name="user.age"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="i5" name="profile.sex"/></th>
                        <td>
                            <c:forEach items="${actionBean.genders}" var="gender">
                                <s:radio id="i5" name="user.sex" value="${gender}"/>${gender}
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <th><s:label for="i6" name="profile.weight"/></th>
                        <td><s:select id="i6" name="user.weightCategory" >
                                <s:options-enumeration enum="cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory" label="showedCategory"/>
                            </s:select></td>
                    </tr>
                </table>
                <s:submit name="register"><f:message key="profile.create"/>
                    <s:param name="user.role" value="${actionBean.defaultRole}"/>
                </s:submit>
                <s:submit name="cancel"><f:message key="profile.cancel"/></s:submit>
                </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>
