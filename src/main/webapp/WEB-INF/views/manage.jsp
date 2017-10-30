<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />

        <link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <!-- MetisMenu CSS -->
        <link href="resources/metisMenu/metisMenu.min.css" rel="stylesheet">

        <!-- Custom CSS -->
        <link href="resources/dist/css/sb-admin-2.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">      

        <link href="resources/selectize/selectize.default.css" rel="stylesheet">

        <link rel='stylesheet' href='resources/bgrins-spectrum-98454b5/spectrum.css' />

        <style>
            .container-table {
                display: table;
            }
            .vertical-center-row {
                display: table-cell;
                vertical-align: middle;
            }
        </style>

    </head>


    <body>

        <input type="hidden" id="_csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/> 

        <div id="wrapper">
            <div class="container container-table">
                <div class="row vertical-center-row">
                    <div class="text-center col-md-4 col-md-offset-4">
                        <h3>PROJECTS :</h3>
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#addNewProject_Modal">Add</button>
                            <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#updateProject_Modal">Update</button>
                            <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#deleteProject_Modal">Delete</button>                            
                        </div>

                        <br><br>                    

                        <h3>Default project :</h3>
                        <div class="form-group">
                            <select class="form-control" id="defaultSchProject_p">
                                <option value="null">N/A</option>
                            </select>
                        </div>

                        <br><br>

                        <h3>LABELS :</h3>
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#addNewSchLabel_Modal">Add</button>
                            <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#updateSchLabel_Modal">Update</button>
                            <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#deleteSchLabel_Modal">Delete</button>                            
                        </div>

                        <br><br><br>

                        <h3>CALENDARS :</h3>
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#addNewCalendar_Modal">Add</button>
                            <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#updateCalendar_Modal">Update</button>
                            <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#deleteCalendar_Modal">Delete</button>                            
                        </div>

                        <br><br>
                        
                        <h3>Default calendar :</h3>
                        <div class="form-group">
                            <select class="form-control" id="defaultSchCalendar_p">
                                <option value="null">N/A</option>
                            </select>
                        </div>
                        
                        <br><br><br>

                        <a href="/WebSchedule/" class="btn btn-default" role="button">BACK</a>
                    </div>
                </div>
            </div>





            <!--------------------------------- Create Modals ---------------------------------------------->

            <div id="addNewProject_Modal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">New project</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <label for="pname">Name:</label>
                                <br>
                                <input type="text" class="form-control" id="addNewSchProject_pname" style="width:80%; float: left;">
                                <br>
                                <br>
                                <label for="sel1" style="float: left;">Select parent:</label>
                                <select class="form-control" id="addNewSchProject_pname_parent">
                                    <option value="#">N/A</option>
                                </select>
                            </div>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default pull-left" id="addNewProject_Modal_c">Create</button>
                        </div>
                    </div>

                </div>
            </div>




            <div id="addNewSchLabel_Modal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">New Label</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <label for="pname">Name:</label>
                                <br>
                                <input type="text" class="form-control" id="addNewSchLabel_pname" style="width:80%;">
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default pull-left" id="addNewSchLabel_Modal_c">Create</button>
                        </div>
                    </div>

                </div>
            </div>



            <div id="addNewCalendar_Modal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">New calendar</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <label for="pname">Name:</label>
                                <br>
                                <input type="text" class="form-control" id="addNewSchCalendar_pname" style="width:80%; float: left;">
                                &nbsp;&nbsp;
                                <input type="text" class="form-control" id="addNewSchCalendar_pcolor" style="float: right;">
                                <br>
                            </div>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default pull-left" id="addNewCalendar_Modal_c">Create</button>
                        </div>
                    </div>

                </div>
            </div>






            <!--------------------------------- Update Modals ---------------------------------------------->

            <div id="updateProject_Modal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Update project</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <select class="form-control" id="updateSchProject_p">
                                    <option value="null">N/A</option>
                                </select>
                            </div>
                            <div class="form-group">  
                                <form>
                                    <fieldset disabled="disabled">
                                        <input type="hidden" id="updateProject_Modal_id" value="">
                                        <label for="pname">Name:</label>
                                        <br>
                                        <input type="text" class="form-control" id="updateSchProject_pname_new" style="width:80%; float: left;">
                                        <br>
                                        <br>
                                        <label for="sel1" style="float: left;">Select parent:</label>
                                        <select class="form-control" id="updateSchProject_pname_parent">
                                            <option value="#">N/A</option>
                                        </select>
                                    </fieldset>                                    
                                </form>                                                                
                            </div>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default pull-left" id="updateProject_Modal_c">Update</button>
                        </div>
                    </div>

                </div>
            </div>




            <div id="updateSchLabel_Modal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Update Label</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <select class="form-control" id="updateSchLabel">
                                    <option value="null">N/A</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <form>
                                    <fieldset disabled="disabled">
                                        <input type="hidden" id="updateLabel_Modal_id" value="">
                                        <label for="pname">Name:</label>
                                        <br>
                                        <input type="text" class="form-control" id="updateSchLabel_pname" style="width:80%; float: left;">
                                    </fieldset>
                                </form>                                
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default pull-left" id="updateSchLabel_Modal_c">Update</button>
                        </div>
                    </div>

                </div>
            </div>




            <div id="updateCalendar_Modal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Update calendar</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <select class="form-control" id="updateSchCalendar_p">
                                    <option value="null">N/A</option>
                                </select>
                            </div>
                            <div class="form-group">  
                                <form>
                                    <fieldset disabled="disabled">
                                        <input type="hidden" id="updateCalendar_Modal_id" value="">
                                        <label for="pname">Name:</label>
                                        <br>
                                        <input type="text" class="form-control" id="updateSchCalendar_pname_new" style="width:80%; float: left;">
                                        &nbsp;&nbsp;
                                        <input type="text" class="form-control" id="updateSchCalendar_pcolor" style="float: right;">
                                        <br>
                                    </fieldset>                                    
                                </form>                                                                
                            </div>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default pull-left" id="updateCalendar_Modal_c">Update</button>
                        </div>
                    </div>

                </div>
            </div>


            <!--------------------------------- Delete Modals ---------------------------------------------->

            <div id="deleteProject_Modal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Delete project & all sub-projects & all tasks</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <select class="form-control" id="deleteSchProject_p">
                                    <option value="null">N/A</option>
                                </select>
                            </div>
                            <div class="form-group">  
                                <form>
                                    <fieldset disabled="disabled">
                                        <input type="hidden" id="deleteProject_Modal_id" value="">
                                        <label for="pname">Type DELETE : </label>
                                        <br>
                                        <input type="text" class="form-control" id="deleteSchProject_pname_new" style="width:80%; float: left;">                                        
                                    </fieldset>                                    
                                </form>                                                                
                            </div>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default pull-left" id="deleteProject_Modal_c">Delete</button>
                        </div>
                    </div>

                </div>
            </div>



            <div id="deleteSchLabel_Modal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Delete Label</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <select class="form-control" id="deleteSchLabel">
                                    <option value="null">N/A</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <form>
                                    <fieldset disabled="disabled">
                                        <input type="hidden" id="deleteLabel_Modal_id" value="">
                                        <label for="pname">Type DELETE : </label>
                                        <br>
                                        <input type="text" class="form-control" id="deleteSchLabel_pname" style="width:80%; float: left;">
                                    </fieldset>
                                </form>                                
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default pull-left" id="deleteSchLabel_Modal_c">Delete</button>
                        </div>
                    </div>

                </div>
            </div>  



            <div id="deleteCalendar_Modal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Delete calendar & all sub-calendars & all tasks</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <select class="form-control" id="deleteSchCalendar_p">
                                    <option value="null">N/A</option>
                                </select>
                            </div>
                            <div class="form-group">  
                                <form>
                                    <fieldset disabled="disabled">
                                        <input type="hidden" id="deleteCalendar_Modal_id" value="">
                                        <label for="pname">Type DELETE : </label>
                                        <br>
                                        <input type="text" class="form-control" id="deleteSchCalendar_pname_new" style="width:80%; float: left;">                                        
                                    </fieldset>                                    
                                </form>                                                                
                            </div>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-default pull-left" id="deleteCalendar_Modal_c">Delete</button>
                        </div>
                    </div>

                </div>
            </div>


            <jsp:include page="errorModal.jsp" />



        </div>

        <!-- jQuery -->
        <script src="resources/jquery/jquery.min.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="resources/bootstrap/js/bootstrap.min.js"></script>

        <!-- Metis Menu Plugin JavaScript -->
        <script src="resources/metisMenu/metisMenu.min.js"></script>

        <!-- Custom Theme JavaScript -->
        <script src="resources/dist/js/sb-admin-2.js"></script>

        <script src="resources/selectize/selectize.js"></script>

        <script src="resources/colors.min.js"></script>

        <script src='resources/bgrins-spectrum-98454b5/spectrum.js'></script>



        <script src="resources/main.js?1112"></script>
        <script src="resources/dbmng.js?1114"></script>
        <script src="resources/manage.js?1214"></script>

    </body>
</html>        