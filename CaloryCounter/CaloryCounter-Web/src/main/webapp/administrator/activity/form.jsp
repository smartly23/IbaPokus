<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<s:layout-component name="header">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
</s:layout-component>
<s:errors/>
<s:hidden name="activity.activityId" value="${actionBean.activity.activityId}"/>
<s:hidden name="activity.deleted" value="${actionBean.activity.deleted}"/>
<table>
    <tr>
        <th><s:label for="activityName" name="activity.name"/></th>
        <td><s:text id="activityName" name="activity.activityName" disabled="${actionBean.delete}"/></td>
    </tr>
    <tr>
        <td>
            <f:message key="activity.calories"/>:
        </td>
    </tr>
    <tr>
        <th><s:label for="130" name="weightCat_130_"/></th>
        <td><s:text id="130" name="activity.weightCalories[${actionBean.categories[0]}]" size="4" disabled="${actionBean.delete}"/></td>
    </tr>
    <tr>
        <th><s:label for="155" name="weightCat_155_"/></th>
        <td><s:text id="155" name="activity.weightCalories[${actionBean.categories[1]}]" size="4" disabled="${actionBean.delete}"/></td>
    </tr>
    <tr>
        <th><s:label for="180" name="weightCat_180_"/></th>
        <td><s:text id="180" name="activity.weightCalories[${actionBean.categories[2]}]" size="4" disabled="${actionBean.delete}"/></td>
    </tr>
    <tr>
        <th><s:label for="205" name="weightCat_205_"/></th>
        <td><s:text id="205" name="activity.weightCalories[${actionBean.categories[3]}]" size="4" disabled="${actionBean.delete}"/></td>
    </tr>
</table>
