<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>

<s:layout-render name="/layout.jsp" titlekey="users.list.title" currentPage="users">
    <s:layout-component name="body">
        <s:useActionBean beanclass="cz.fi.muni.pa165.calorycounter.frontend.UsersAdministrationActionBean" var="actionBean"/>

        <h2><f:message key="users.list.title"/></h2>
        <c:choose>
            <c:when test="${not empty actionBean.users}">
                <table class="basic" id="users">
                    <thead>
                        <tr>
                            <th><f:message key="user.username"/></th>
                            <th><f:message key="user.name"/></th>
                            <th><f:message key="user.sex"/></th>
                            <th><f:message key="user.age"/></th>
                            <th><f:message key="user.weight"/></th>
                            <th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${actionBean.users}" var="user">
                            <tr>
                                <td><c:out value="${user.username}"/></td>
                                <td><c:out value="${user.name}"/></td>
                                <td><f:message key="gender.${user.sex}"/></td>
                                <td><c:out value="${user.age}"/></td>
                                <td><f:message key="weightCat${user.weightCategory.name}"/></td>
                                <td>
                                    <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.UsersAdministrationActionBean" event="delete" class="button"><s:param name="user.userId" value="${user.userId}" /><f:message key="delete"/></s:link>
                                    </td>
                                </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p><f:message key="users.list.norecord"/></p>
            </c:otherwise>
        </c:choose>
    </s:layout-component>
</s:layout-render>