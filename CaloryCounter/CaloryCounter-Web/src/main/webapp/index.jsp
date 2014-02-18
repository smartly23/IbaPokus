<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<s:layout-render name="/layout.jsp" titlekey="home.titlekey" currentPage="home">
    <s:layout-component name="body">
        <h2><f:message key="home.title"/></h2>

        <img id="photo" src="${pageContext.request.contextPath}/images/home_picture.jpg" alt="<f:message key="home.photo.alt"/>" />

        <p class="homeP"><f:message key="home.text.p1"/></p>
        <p id="lastP"><f:message key="home.text.p2"/></p>

    </s:layout-component>
</s:layout-render>
