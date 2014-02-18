<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<s:layout-render name="/layout.jsp" titlekey="records.titlekey">
    <s:layout-component name="body">

        <h2><f:message key="records.title"/></h2>

        <form id="searchForm" action="/records.jsp" method="post">
            <label for="username"><f:message key="records.findbyusername"/>:</label>
            <input id="searchUsername" name="searchUsername" value=""/>
            <input type="submit" name="search" value="<f:message key="records.findBtn"/>"/>
            <input type="submit" name="new" value="<f:message key="records.newBtn"/>" style="margin-left: 300px"/>
        </form>

        <div id="stateBar"><f:message key="records.state"/>: <span id="state"></span></div>

        <form id="recordForm" action="/records.jsp" method="post">
            <div id="recordsList" style="display: none">

            </div>
        </form>

        <div id="recordInfo" style="display: none">
            <form id="recForm" action="/records.jsp" method="post">
                <div>
                    <label for="date"><f:message key="record.date"/>:</label>
                    <input id="date" name="date" value=""/>
                </div>
                <div>
                    <label for="activity"><f:message key="record.activity"/>:</label>
                    <select id="activity" name="activity">
                    </select>
                </div>
                <div>
                    <label for="duration"><f:message key="record.duration"/>:</label>
                    <input id="duration" name="duration" value=""/>
                </div>
                <div id="buttons">
                    <input type="hidden" id="userId" name="userId" value=""/>
                    <input type="hidden" id="usernameKey" name="usernameKey" value=""/>
                    <input type="submit" name="create" value="<f:message key="records.createBtn"/>"/>
                </div>
            </form>
        </div>

        <script type="text/javascript">
            $(function() {
                var request = $.ajax({
                    url: "http://localhost:8080/CaloryCounter-Web/resources/activities",
                    type: "GET",
                    dataType: "json",
                    crossDomain: true,
                    success: getActSuccess
                });
                function getActSuccess(data) {
                    for (var i = 0; i < data.length; i++) {
                        var $table = $('<option/>');
                        $table.val(data[i].activityName);
                        $table.append(data[i].activityName);
                        $("#activity").append($table);
                    }

                }
            });

            $("#searchForm").submit(function(event) {
                if ($("#searchForm input[type=submit][clicked=true]").attr("name") == "search") {
                    $("#state").text("<f:message key="records.state.searching"/>");
                    var request = $.ajax({
                        url: "http://localhost:8080/CaloryCounter-Web/resources/record/user/" + $("#searchUsername").val(),
                        type: "GET",
                        dataType: "json",
                        crossDomain: true,
                        success: getUserSuccess
                    });
                    request.fail(function(jqXHR, textStatus) {
                        $("#state").text("<f:message key="records.state.notFound"/>");
                        $("#recordsList").hide();
                        $("#recordInfo").hide();
                    });
                } else if ($("#searchForm input[type=submit][clicked=true]").attr("name") == "new") {
                    if ($("#userId").val() == "") {
                        $("#state").text("<f:message key="records.state.emptyUsername"/>");
                    } else {
                        //$("#userForm input[name='create']").show();
                        $("#state").text("");
                        $("#recordsList").hide();
                        $("#recordInfo").show();
                        $("#date").val("");
                        $("#activity").val("");
                        $("#duration").val("");
                        $("#usernameKey").val("");
                    }
                }
                event.preventDefault();
            });
            function getUserSuccess(data) {
                $("#state").text("<f:message key="records.state.found"/>");

                var $table = $('<table/>');
                var line = "";
                $table.addClass('basic');
                $table.append('<tr><th><f:message key="record.date"/> </th><th><f:message key="record.activity"/> </th><th><f:message key="record.burnt_calories"/> </th><th><f:message key="record.weight"/> </th><th><f:message key="record.duration"/> </th><th></th><th></th></tr>');
                for (var i = 0; i < data.activityRecords.length; i++) {
                    $("#userId").val(data.activityRecords[i].userId + '');
                    console.log(data.activityRecords[i].userId);
                    line = '<tr id="row' + data.activityRecords[i].activityRecordId + '"><td><input name="date' + data.activityRecords[i].activityRecordId + '" id="date' + data.activityRecords[i].activityRecordId + '" value="' + data.activityRecords[i].activityDate + '"/></td>';
                    line += '<td>  <input name="activity' + data.activityRecords[i].activityRecordId + '" value="' + data.activityRecords[i].activityName + '" disabled /></td>';
                    line += '<td><input  style="width:50px"  name="burnt_calories' + data.activityRecords[i].activityRecordId + '" value="' + data.activityRecords[i].caloriesBurnt + '"/></td>';
                    line += '<td><select disabled name="weight' + data.activityRecords[i].activityRecordId + '"><option value="_130_"><f:message key="records.weightCat._130_"/></option><option value="_155_"><f:message key="records.weightCat._155_"/></option><option value="_180_"><f:message key="records.weightCat._180_"/></option><option value="_205_"><f:message key="records.weightCat._205_"/></option></select></td>';
                    line += '<td>  <input name="duration' + data.activityRecords[i].activityRecordId + '" value="' + data.activityRecords[i].duration + '"/></td>';
//' + data.activityRecords[i].weightCategory + '
                    line += '<td><input type="submit" name="edit" data-id="' + data.activityRecords[i].activityRecordId + '" data-userid="' + data.activityRecords[i].userId + '" value="<f:message key="records.editBtn"/>"/></td>';
                    line += '<td><input type="submit" name="delete" data-id="' + data.activityRecords[i].activityRecordId + '" data-userid="' + data.activityRecords[i].userId + '" value="<f:message key="records.deleteBtn"/>"/></td></tr>';
                    $table.append(line);
                }
                $("#recordsList").html($table);
                for (var i = 0; i < data.activityRecords.length; i++) {
                    $('#date' + data.activityRecords[i].activityRecordId).datepicker();
                    $('#date' + data.activityRecords[i].activityRecordId).datepicker("option", "dateFormat", "yy-mm-ddT00:00:00");
                    $('#date' + data.activityRecords[i].activityRecordId).datepicker("setDate", data.activityRecords[i].activityDate);
                    $('#weight' + data.activityRecords[i].activityRecordId).val(data.activityRecords[i].weightCategory);

                }

                $("#userForm input[name='edit']").hide();
                $("#userForm input[name='delete']").hide();
                $("#userForm input[name='create']").hide();
                $("#recordInfo").hide();
                $("#recordsList").show();
                /*
                 $("#username").val(data.username);
                 $("#password").val(data.password);
                 $("#name").val(data.name);
                 $("#age").val(data.age);
                 $("#sex").val(data.sex);
                 $("#weightCategory").val(data.weightCategory);
                 $("#usernameKey").val(data.username);
                 $("#userId").val(data.userId);*/
            }


            $("#recordForm").submit(function(event) {
                activityRecordId = $("#recordForm input[type=submit][clicked=true]").data("id");
                console.debug(activityRecordId);
                if (activityRecordId == "") {
                    $("#state").text("<f:message key="records.state.emptyUsername"/>");
                } else if ($("#recordForm input[type=submit][clicked=true]").attr("name") == "edit") {
                    $("#state").text("<f:message key="records.state.updating"/>");
                    var data = '{ "activityDate" : "' + $("#recordForm input[name=date" + activityRecordId + "]").val() + '", ' +
                            '"activityName" : "' + $("#recordForm input[name=activity" + activityRecordId + "]").val() + '", ' +
                            '"activityRecordId" : "' + activityRecordId + '", ' +
                            '"caloriesBurnt" : "' + $("#recordForm input[name=burnt_calories" + activityRecordId + "]").val() + '", ' +
                            '"duration" : "' + $("#recordForm input[name=duration" + activityRecordId + "]").val() + '", ' +
                            '"userId" : "' + $("#recordForm input[type=submit][clicked=true]").data("userid") + '", ' +
                            '"weightCategory" : "' + $("#recordForm select[name=weight" + activityRecordId + "]").val() + '" }';
                    var request = $.ajax({
                        url: "http://localhost:8080/CaloryCounter-Web/resources/record/update",
                        type: "PUT",
                        dataType: "json",
                        crossDomain: true,
                        contentType: "application/json; charset=utf-8",
                        data: data,
                        success: editUserSuccess
                    });
                    request.fail(function(jqXHR, textStatus) {
                        $("#state").text("<f:message key="records.state.notUpdated"/>");
                        $("#recordsList").hide();
                    });
                } else if ($("#recordForm input[type=submit][clicked=true]").attr("name") == "delete") {
                    $("#state").text("<f:message key="records.state.deleting"/>");
                    var request = $.ajax({
                        url: "http://localhost:8080/CaloryCounter-Web/resources/record/remove/" + activityRecordId,
                        type: "DELETE",
                        contentType: "application/json; charset=utf-8",
                        crossDomain: true,
                        success: deleteUserSuccess
                    });
                    request.fail(function(jqXHR, textStatus) {
                        $("#state").text("<f:message key="records.state.notDeleted"/> (" + textStatus + ")");
                        $("#recordsList").hide();
                    });
                }
                event.preventDefault();
            });
            $("#recForm").submit(function(event) {
                if ($("#userId").val() == "") {
                    $("#state").text("<f:message key="records.state.emptyUsername"/>");
                } else {
                    $("#state").text("<f:message key="records.state.creating"/>");
                    var data = '{ "activityDate" : "' + $("#date").val() + '", ' +
                            '"activityName" : "' + $("#activity").val() + '", ' +
                            '"duration" : "' + $("#duration").val() + '", ' +
                            '"userId" : "' + $("#userId").val() + '"}';

                    var request = $.ajax({
                        url: "http://localhost:8080/CaloryCounter-Web/resources/record/create",
                        type: "POST",
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        crossDomain: true,
                        data: data,
                        success: createUserSuccess
                    });
                    request.fail(function(jqXHR, textStatus) {
                        $("#state").text("<f:message key="records.state.notCreated"/>");
                    });
                }
                event.preventDefault();
            });
            function editUserSuccess(data) {
                $("#state").text("<f:message key="records.state.updated"/>");
                $("#recordsList").hide();
            }
            function deleteUserSuccess() {
                $("#state").text("<f:message key="records.state.deleted"/>");
                $("#recordsList").hide();
                $("#searchUsername").val("");
            }
            function createUserSuccess(data) {
                $("#state").text("<f:message key="records.state.created"/>");
                $("#recordInfo").hide();
            }

            $("#recordForm").on('click', 'input[type=submit]', function() {
                $("#recordForm input[type=submit]").removeAttr("clicked");
                $(this).attr("clicked", "true");
            });
            $("#searchForm").on('click', 'input[type=submit]', function() {
                $("#searchForm input[type=submit]").removeAttr("clicked");
                $(this).attr("clicked", "true");
            });


            $(function() {
                $("#date").datepicker();
                $("#date").datepicker("option", "dateFormat", "yy-mm-ddT00:00:00");
                /*
                 
                 var request = $.ajax({
                 url: "http://localhost:8080/CaloryCounter-Web/resources/record/create",
                 type: "POST",
                 dataType: "json",
                 contentType: "application/json; charset=utf-8",
                 crossDomain: true,
                 data: data,
                 success: createUserSuccess
                 });
                 request.fail(function(jqXHR, textStatus) {
                 $("#state").text("<f:message key="records.state.notCreated"/>");
                 });*/

            });

        </script>

    </s:layout-component>
</s:layout-render>
