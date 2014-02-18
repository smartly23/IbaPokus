<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="profile.edit">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.ProfileActionBean" var="actionBean"/>
        <s:errors/>
        <s:form beanclass="cz.fi.muni.pa165.calorycounter.frontend.ProfileActionBean">
            <fieldset><legend><f:message key="profile.edit"/></legend>
                <table>
                    <tr>
                        <!-- "for" label-u je previazane s "id" text-u, tzn. ze klik na dany label
                        sposobi akciu na danom text inpute; "name" labelu znaci to, co sa zobrazi v casti
                        label a mozem to byt aj z toho properties suboru, ako v tomto pripade, alebo
                        natvrdo medzi label a /label tagmi-->
                        <th><s:label for="i1" name="profile.name"/></th>
                        <td><s:text id="i1" name="user.name"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="i2" name="profile.age"/></th>
                        <td><s:text id="i2" name="user.age"/></td>
                    </tr>
                    <tr>
                        <th><s:label for="i3" name="profile.sex"/></th>
                        <td>
                            <c:forEach items="${actionBean.genders}" var="gender">
                                <s:radio id="i3" name="user.sex" value="${gender}"/><f:message key="gender.${gender.name}"/>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <th><s:label for="i4" name="profile.weight"/></th>
                        <td><s:select id="i4" name="user.weightCategory" >
                                <c:forEach items="${actionBean.weightCategories}" var="category">
                                    <s:option value="${category}"><f:message key="weightCat${category.name}"/></s:option>
                                </c:forEach>
                            </s:select></td>
                    </tr>
                </table>
                <s:submit name="save"><f:message key="profile.save"/>
                    <s:param name="user.username" value="${actionBean.user.username}"/>
                    <s:param name="user.role" value="${actionBean.user.role}"/>
                </s:submit>
                <s:submit name="cancel"><f:message key="profile.cancel"/></s:submit>
                </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>
