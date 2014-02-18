<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<s:layout-render name="/layout.jsp" titlekey="users.titlekey">
    <s:layout-component name="body">

        <h2><f:message key="users.title"/></h2>

        <form id="searchForm" action="/users.jsp" method="post">
            <label for="username"><f:message key="users.findbyusername"/>:</label>
            <input id="searchUsername" name="searchUsername" value=""/>
            <input type="submit" name="search" value="<f:message key="users.findBtn"/>"/>
            <input type="submit" name="new" value="<f:message key="users.newBtn"/>" style="margin-left: 300px"/>
        </form>

        <div id="stateBar"><f:message key="users.state"/>: <span id="state"></span></div>
        <div id="userInfo" style="display: none">
            <form id="userForm" action="/users.jsp" method="post">
                <div>
                    <label for="username"><f:message key="users.username"/>:</label>
                    <input id="username" name="username" value=""/>
                </div>
                <div>
                    <label for="name"><f:message key="users.name"/>:</label>
                    <input id="name" name="name" value=""/>
                </div>
                <div>
                    <label for="age"><f:message key="users.age"/>:</label>
                    <input id="age" name="age" value=""/>
                </div>
                <div>
                    <label for="sex"><f:message key="users.sex"/>:</label>
                    <select id="sex" name="sex">
                        <option value="Male"><f:message key="users.sex.male"/></option>
                        <option value="Female"><f:message key="users.sex.female"/></option>
                        <option value="Other"><f:message key="users.sex.other"/></option>
                    </select>
                </div>
                <div>
                    <label for="weightCategory"><f:message key="users.weightCategory"/>:</label>
                    <select id="weightCategory" name="weightCategory">
                        <option value="_130_"><f:message key="users.weightCat._130_"/></option>
                        <option value="_155_"><f:message key="users.weightCat._155_"/></option>
                        <option value="_180_"><f:message key="users.weightCat._180_"/></option>
                        <option value="_205_"><f:message key="users.weightCat._205_"/></option>
                    </select>
                </div>
                <div id="buttons">
                    <input type="hidden" id="userId" name="userId" value=""/>
                    <input type="hidden" id="usernameKey" name="usernameKey" value=""/>
                    <input type="submit" name="create" value="<f:message key="users.createBtn"/>"/>
                    <input type="submit" name="edit" value="<f:message key="users.editBtn"/>"/>
                    <input type="submit" name="delete" value="<f:message key="users.deleteBtn"/>"/>
                </div>
                <div> <!--
      <label for="password"><f:message key="users.password"/>:</label>
      <input id="password" name="password" value=""/>-->
                    * default password - "password"
                </div>
            </form>
        </div>

        <script type="text/javascript">
            $("#searchForm").submit(function(event) {
                if ($("#searchForm input[type=submit][clicked=true]").attr("name") == "search") {
                    $("#state").text("<f:message key="users.state.searching"/>");
                    var request = $.ajax({
                        url: "http://localhost:8080/CaloryCounter-Web/resources/profile/getuser/" + $("#searchUsername").val(),
                        type: "GET",
                        dataType: "json",
                        crossDomain: true,
                        success: getUserSuccess
                    });
                    request.fail(function(jqXHR, textStatus) {
                        $("#state").text("<f:message key="users.state.notFound"/>");
                        $("#userInfo").hide();
                    });
                } else if ($("#searchForm input[type=submit][clicked=true]").attr("name") == "new") {
                    $("#userForm input[name='edit']").hide();
                    $("#userForm input[name='delete']").hide();
                    $("#userForm input[name='create']").show();
                    $("#state").text("");
                    $("#userInfo").show();
                    $("#username").val("");
                    $("#username").prop('disabled', false);
                    $("#password").val("");
                    $("#password").prop('disabled', false);
                    $("#name").val("");
                    $("#age").val("");
                    $("#sex").val("<f:message key='users.sex.male'/>");
                    $("#weightCategory").val("_155_");
                    $("#userId").val("");
                    $("#usernameKey").val("");
                }
                event.preventDefault();
            });
            function getUserSuccess(data) {
                $("#state").text("<f:message key="users.state.found"/>");
                $("#userForm input[name='edit']").show();
                $("#userForm input[name='delete']").show();
                $("#userForm input[name='create']").hide();
                $("#userInfo").show();
                $("#username").val(data.username);
                $("#username").prop('disabled', true);
                $("#password").val(data.password);
                $("#password").prop('disabled', true);
                $("#name").val(data.name);
                $("#age").val(data.age);
                $("#sex").val(data.sex);
                $("#weightCategory").val(data.weightCategory);
                $("#usernameKey").val(data.username);
                $("#userId").val(data.userId);
            }


            $("#userForm").submit(function(event) {
                if ($("#username").val() == "") {
                    $("#state").text("<f:message key="users.state.emptyUsername"/>");
                } else if ($("#userForm input[type=submit][clicked=true]").attr("name") == "edit") {
                    $("#state").text("<f:message key="users.state.updating"/>");
                    var data = '{ "username" : "' + $("#username").val() + '", ' +
                            '"userId" : "' + $("#userId").val() + '", ' +
                            '"password" : "' + $("#password").val() + '", ' +
                            '"name" : "' + $("#name").val() + '", ' +
                            '"sex" : "' + $("#sex").val() + '", ' +
                            '"weightCategory" : "' + $("#weightCategory").val() + '", ' +
                            '"age" : "' + $("#age").val() + '" }';
                    var request = $.ajax({
                        url: "http://localhost:8080/CaloryCounter-Web/resources/profile/updateuser",
                        type: "PUT",
                        dataType: "json",
                        crossDomain: true,
                        contentType: "application/json; charset=utf-8",
                        data: data,
                        success: editUserSuccess
                    });
                    request.fail(function(jqXHR, textStatus) {
                        $("#state").text("<f:message key="users.state.notUpdated"/>");
                        $("#userInfo").hide();
                    });
                } else if ($("#userForm input[type=submit][clicked=true]").attr("name") == "delete") {
                    $("#state").text("<f:message key="users.state.deleting"/>");
                    var request = $.ajax({
                        url: "http://localhost:8080/CaloryCounter-Web/resources/profile/removeuser/" + $("#usernameKey").val(),
                        type: "DELETE",
                        contentType: "application/json; charset=utf-8",
                        crossDomain: true,
                        success: deleteUserSuccess
                    });
                    request.fail(function(jqXHR, textStatus) {
                        $("#state").text("<f:message key="users.state.notDeleted"/> (" + textStatus + ")");
                        $("#userInfo").hide();
                    });
                } else if ($("#userForm input[type=submit][clicked=true]").attr("name") == "create") {
                    $("#state").text("<f:message key="users.state.creating"/>");
                    var data = '{ "username" : "' + $("#username").val() + '", ' +
                            '"password" : "' + $("#password").val() + '", ' +
                            '"name" : "' + $("#name").val() + '", ' +
                            '"sex" : "' + $("#sex").val() + '", ' +
                            '"weightCategory" : "' + $("#weightCategory").val() + '", ' +
                            '"age" : "' + $("#age").val() + '" }';
                    var request = $.ajax({
                        url: "http://localhost:8080/CaloryCounter-Web/resources/profile/createuser",
                        type: "POST",
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        crossDomain: true,
                        data: data,
                        success: createUserSuccess
                    });
                    request.fail(function(jqXHR, textStatus) {
                        $("#state").text("<f:message key="users.state.notCreated"/>");
                    });
                }
                event.preventDefault();
            });
            function editUserSuccess(data) {
                $("#state").text("<f:message key="users.state.updated"/>");
                $("#userInfo").hide();
            }
            function deleteUserSuccess() {
                $("#state").text("<f:message key="users.state.deleted"/>");
                $("#userInfo").hide();
                $("#searchUsername").val("");
            }
            function createUserSuccess(data) {
                $("#state").text("<f:message key="users.state.created"/>");
                $("#userInfo").hide();
            }

            $("#userForm input[type=submit]").click(function() {
                $("#userForm input[type=submit]").removeAttr("clicked");
                $(this).attr("clicked", "true");
            });
            $("#searchForm input[type=submit]").click(function() {
                $("#searchForm input[type=submit]").removeAttr("clicked");
                $(this).attr("clicked", "true");
            });

        </script>

    </s:layout-component>
</s:layout-render>
