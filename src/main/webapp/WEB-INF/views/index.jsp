<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="initial-scale=1" />

        <link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <link href="resources/metisMenu/metisMenu.min.css" rel="stylesheet">

        <link href="resources/dist/css/sb-admin-2.css" rel="stylesheet">

        <link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <link href="resources/jstree/themes/proton/style.min.css" rel="stylesheet" />

        <link href="resources/pickadate.js-3.5.6/themes/classic.css" rel="stylesheet"> 
        <link href="resources/pickadate.js-3.5.6/themes/classic.date.css" rel="stylesheet">
        <link href="resources/pickadate.js-3.5.6/themes/classic.time.css" rel="stylesheet">

        <link href="resources/selectize/selectize.default.css" rel="stylesheet">

        <link href='resources/fullcalendar-3.4.0/fullcalendar.min.css' rel='stylesheet' />    


        <link href="resources/index.css?1112" rel="stylesheet">

    </head>


    <body>

        <input type="hidden" id="_csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/> 

        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0;">
                <div class="navbar-header">                    
                    <div class="sidebar-search">
                        <div class="input-group custom-search-form">
                            <input type="text" class="form-control" placeholder="Search..." id="searchInput">
                            <span class="input-group-btn">
                                <button class="btn btn-default" type="button" id="searchButton">
                                    <i class="fa fa-search"></i>
                                </button>
                            </span>
                        </div>
                        <!-- /input-group -->
                    </div>
                </div>

                <ul class="nav navbar-top-links navbar-right">      
                    <li>
                        <a role="button" class="btn" data-toggle="collapse" data-target=".navbar-collapse" id="mobileMenuButton">
                            <i class="fa fa-bars fa-fw"></i>
                        </a>
                    </li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                            <i class="fa fa-gg fa-fw"></i> <i class="fa fa-caret-down"></i>
                        </a>
                        <ul class="dropdown-menu" id="sch_labels">

                        </ul>                        
                    </li>

                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                            <i class="fa fa-calendar-check-o"></i> <i class="fa fa-caret-down"></i>
                        </a>
                        <ul class="dropdown-menu" id="selectedCalendars">

                        </ul> 
                    </li>                
                    <li>
                        <a href="#" class="btn" role="button" id="showCalendar">
                            <i class="fa fa-calendar"></i>
                        </a> 
                    </li>  
                    <li>
                        <a href="/WebSchedule/m" class="btn btn-default" role="button">M</a>     
                    </li>
                    <li>
                        <form method="post" action="${pageContext.request.contextPath}/logout">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>         
                            <button type="submit" class="btn btn-default" role="button">OUT</button>
                        </form> 
                    </li>
                </ul>     

                <div class="navbar-default sidebar" role="navigation" style="padding-top: 10px;">
                    <div class="sidebar-nav navbar-collapse collapse">
                        <ul class="nav" id="side-menu">

                        </ul>                        
                    </div>
                </div>
            </nav>

            <div id="page-wrapper">

                <div id="tasks-wrapper">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">                                                          
                                <div class="btn-group dropdown gs">
                                    <button class="btn dropdown-toggle" data-toggle="dropdown"><span id="" class="fa fa-group"></span> Group <span class="caret"></span></button>
                                    <ul class="dropdown-menu ulGroup">
                                        <li><a href="#">Project <span id="group-project" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                        <li><a href="#">Label <span id="group-label" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                        <li><a href="#"><span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span> Start date/time <span id="group-sdate-up" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                        <li><a href="#"><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span> Start date/time <span id="group-sdate-down" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                        <li class="divider"></li>
                                        <li><a href="#">Don't group <span id="dont-group" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                    </ul>
                                </div>
                                <div class="btn-group dropdown gs">
                                    <button class="btn dropdown-toggle" data-toggle="dropdown"><span id="" class="fa fa-sort"></span> Sort <span class="caret"></span></button>
                                    <ul class="dropdown-menu ulSort">
                                        <li><a href="#"><span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span> Start date/time <span id="sort-sdate-up" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                        <li><a href="#"><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span> Start date/time <span id="sort-sdate-down" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                        <li><a href="#"><span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span> Priority <span id="sort-prio-up" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                        <li><a href="#"><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span> Priority <span id="sort-prio-down" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                        <li class="divider"></li>
                                        <li><a href="#">Don't sort <span id="dont-sort" class="gsli glyphicon" aria-hidden="true"></span></a></li>
                                    </ul>
                                </div>
                            </h1>
                        </div>

                    </div>

                    <div class="row">
                        <div class="col-lg-4 width44" id="tasks_list_content">

                        </div>
                        <!-- /.col-lg-4 -->
                        <div class="col-lg-4 width44" id="ts_info">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    Ts info
                                </div>
                                <div class="panel-body fbody" id="formBodyDesktop">                                    
                                    <jsp:include page="indexTaskForm.jsp" />
                                </div>
                            </div>
                        </div>
                    </div>

                </div> <!--end tasks-wrapper-->

                <br><br>

                <div id="calendar-wrapper-2">
                    <div id="calendar-wrapper">
                        <div id='calendar'></div>
                    </div> <!--end calendar-wrapper-->
                </div>                
                <br>

            </div> <!--end page-wrapper-->



            <jsp:include page="indexModals.jsp" />

            <jsp:include page="templates.jsp" />

        </div>


        <script src="resources/jquery/jquery.min.js"></script>

        <script src="resources/bootstrap/js/bootstrap.min.js"></script>  

        <script src="resources/metisMenu/metisMenu.min.js"></script>

        <script src="resources/dist/js/sb-admin-2.js"></script>


        <script src="resources/jstree/jstree.js?1111"></script>        


        <script src="resources/pickadate.js-3.5.6/picker.js"></script>
        <script src="resources/pickadate.js-3.5.6/picker.date.js"></script>
        <script src="resources/pickadate.js-3.5.6/picker.time.js"></script>

        <script src="resources/textboxio/textboxio.js"></script>

        <script src="resources/selectize/selectize.js"></script>

        <script src='resources/fullcalendar-3.4.0/lib/moment.min.js'></script>
        <script src='resources/fullcalendar-3.4.0/fullcalendar.min.js?1234'></script>

        <script src="resources/colors.min.js"></script>



        <script src="resources/main.js?1118"></script>
        <script src="resources/dbmng.js?1122"></script>
        <script src="resources/index.js?1593"></script>
        <script src="resources/indexDateTimePicker.js?1574"></script>
        <script src="resources/indexEditLink.js?1574"></script>
        <script src="resources/indexEditTaskLabels.js?1574"></script>
        <script src="resources/indexEditTaskParent.js?1574"></script>
        <script src="resources/indexEditTaskProjects.js?1574"></script>
        <script src="resources/indexGroupAndSort.js?1575"></script>
        <script src="resources/indexProjectsMenu.js?1574"></script>        
        <script src="resources/indexTaskLists.js?1574"></script>
        <script src="resources/indexCalendars.js?1575"></script>

    </body>
</html>
