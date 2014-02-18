<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<s:layout-component name="header">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script>
        $.datepicker.setDefaults({
            showOn: "both",
            dateFormat: "<f:message key="date.format.datepicker"/>"
        });
        $(function() {
            $("#activityDate").datepicker();
        });
    </script>
</s:layout-component>
<s:errors/>
<s:hidden name="record.userId" value="${actionBean.user.userId}"/>
<s:hidden name="record.weightCategory" value="${actionBean.user.weightCategory}"/>
<table>
    <tr>
        <th><s:label for="activityName" name="record.activity"/></th>
        <td><s:select id="activityName" name="record.activityName" disabled="${actionBean.isEdit}"><s:options-collection collection="${actionBean.activities}" value="activityName" label="activityName"/></s:select></td>
        </tr>
        <tr>
            <th><s:label for="duration" name="record.duration"/></th>
        <td><s:text id="duration" name="record.duration" size="4"/> <f:message key="minutes"/></td>
    </tr>
    <tr>
        <th><s:label for="activityDate" name="record.date"/></th>
        <td><s:text id="activityDate" name="record.activityDate" formatPattern="${dateFormat}"/></td>
    </tr>
</table>
