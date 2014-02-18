<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<s:layout-definition>
    <!DOCTYPE html>
    <html lang="${pageContext.request.locale}">
        <head>
            <title><f:message key="${titlekey}"/></title>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css" />
            <script type='text/javascript' src='http://code.jquery.com/jquery.min.js'></script>
            <!-- DataTables -->
            <link rel="stylesheet" type="text/css" href="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css">
            <script type="text/javascript" charset="utf8" src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js"></script>
            <script type="text/javascript" charset="utf-8">
                $(function() {
                    $('#nav_${currentPage}').addClass("current");
                });
                $(document).ready(function() {
                    $.extend($.fn.dataTable.defaults, {
                        "bStateSave": true,
                        "sPaginationType": "two_button",
                        "oLanguage": {
                            "sProcessing": "<f:message key="dataTables.processing"/>",
                            "sLengthMenu": "<f:message key="dataTables.lengthMenu"/>",
                            "sZeroRecords": "<f:message key="dataTables.zeroRecords"/>",
                            "sInfo": "<f:message key="dataTables.info"/>",
                            "sInfoEmpty": "<f:message key="dataTables.infoEmpty"/>",
                            "sInfoFiltered": "<f:message key="dataTables.infoFiltered"/>",
                            "sInfoPostFix": "",
                            "sSearch": "<f:message key="dataTables.search"/>",
                            "sUrl": "",
                            "oPaginate": {
                                "sFirst": "<f:message key="dataTables.first"/>",
                                "sPrevious": "<f:message key="dataTables.previous"/>",
                                "sNext": "<f:message key="dataTables.next"/>",
                                "sLast": "<f:message key="dataTables.laset"/>"
                            },
                            "oAria": {
                                "sSortAscending": "<f:message key="dataTables.sortAscending"/>",
                                "sSortDescending": "<f:message key="dataTables.sortDescending"/>"
                            }
                        }
                    });
                    var activTable = $('#activities').dataTable({
                        "aaSorting": [[0, "desc"]]
                    });
                    var activTableAdmin = $('#activitiesAdmin').dataTable({
                        "aaSorting": [[0, "desc"]],
                        "aoColumnDefs": [
                            {"bSortable": false, "aTargets": [5]}
                        ]
                    });
                    var recordsTable = $('#records').dataTable({
                        "aaSorting": [[0, "desc"]],
                        "bFilter": false
                    });
                    var ladderTable = $('#globalLadder').dataTable({
                        "aoColumnDefs": [
                            {"bSortable": false, "aTargets": [0, 1]}
                        ],
                        "aaSorting": [[2, "desc"]]
                    });
                    var usersTable = $('#users').dataTable({
                        "aaSorting": [[0, "asc"]],
                        "aoColumnDefs": [
                            {"bSortable": false, "aTargets": [5]}
                        ]
                    });
                    var tables = [activTable, recordsTable, ladderTable, activTableAdmin, usersTable];
                    tables.forEach($('td').hover(function() {
                        $(this.parentNode).addClass('highlighted');
                    }, function() {
                        tables.forEach($('tr.highlighted').removeClass('highlighted'));
                    }));
                });
            </script>
            <s:layout-component name="header"/>
            <c:set var="dateFormat" scope="session"><f:message key="date.format.jstl"/></c:set>
            </head>

            <body>
                <div id="main_container">
                    <div id="header">
                        <div id="logo"><s:link href="/index.jsp"><img src="${pageContext.request.contextPath}/images/logo.png" alt="logo" title="${topTitle}"/></s:link></div>
                    <h1 id="topTitle"><s:link href="/index.jsp"><f:message key="topTitle"/></s:link></h1>
                        <div id="profile"><p>
                            <c:if test="${sessionScope.user!=null}"><f:message key="profile.as"/> <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.ProfileActionBean">${sessionScope.user.name}</s:link><br /></c:if>
                            <c:choose>
                                <c:when test="${sessionScope.user!=null}">
                                    <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.AuthenticationActionBean" event="logout"><f:message key="profile.logout"/></s:link>
                                </c:when>
                                <c:otherwise>
                                    <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.AuthenticationActionBean"><f:message key="profile.login"/></s:link><br />
                                    <s:link beanclass="cz.fi.muni.pa165.calorycounter.frontend.AuthenticationActionBean" event="showRegisterForm"><f:message key="profile.register"/></s:link>
                                </c:otherwise>
                            </c:choose>
                        </p></div>
                    <div id="menu">
                        <ul>
                            <li id="first"><s:link id="nav_home" href="/index.jsp"><f:message key="navigation.home"/></s:link></li>
                            <c:if test="${sessionScope.user!=null}"><li><s:link id="nav_new_record" beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordActionBean"><f:message key="navigation.new_record"/></s:link></li></c:if>
                            <c:if test="${sessionScope.user!=null}"><li><s:link id="nav_my_records" beanclass="cz.fi.muni.pa165.calorycounter.frontend.RecordsActionBean"><f:message key="navigation.my_records"/></s:link></li></c:if>
                            <li><s:link id="nav_global_ladder" beanclass="cz.fi.muni.pa165.calorycounter.frontend.StatsActionBean"><f:message key="navigation.global_ladder"/></s:link></li>
                            <li><s:link id="nav_activities" beanclass="cz.fi.muni.pa165.calorycounter.frontend.ActivitiesActionBean"><f:message key="navigation.activities"/></s:link></li>
                            <c:if test="${sessionScope.user!=null && sessionScope.user.role=='ADMIN'}"><li><s:link id="nav_users" beanclass="cz.fi.muni.pa165.calorycounter.frontend.UsersAdministrationActionBean"><f:message key="navigation.users"/></s:link></li></c:if>
                                </ul>
                            </div>
                        </div>

                <s:messages/>

                <div id="main_content">
                    <s:layout-component name="body"/>
                    <div style=" clear:both;"></div>
                </div><!--end of main content-->


                <div id="footer">
                    <div class="copyright">
                        (c) MUFI
                    </div>
                    <div class="footer_links">
                    </div>
                </div>
            </div> <!--end of main container-->
        </body>
    </html>
</s:layout-definition>
