<%-- 
    Document   : modals
    Created on : Aug 17, 2017, 2:35:08 PM
    Author     : ivaylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#editTask_Modal" style="display: none;" id="editTask_Modal_Button"></button>
<div id="editTask_Modal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Ts info</h4>
            </div>
            <div class="modal-body fbody" id="formBodyModal">           
                <jsp:include page="indexTaskForm.jsp" />
            </div>
        </div>
    </div>
</div>


<div id="editNotes_Modal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Ts notes</h4>
            </div>
            <div class="modal-body">
                <textarea class="textarea-sch"></textarea>
            </div>
        </div>
    </div>
</div>    

<div id="editLinks_Modal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Ts links</h4>
            </div>
            <div class="modal-body">                
                <div class="form-group">
                    <input id="link_input" type="text" placeholder="Write up something" style="width: 75%;">
                    <button type="button" class="btn btn-success" id="add_link_button" style="float: right;">Add</button>
                </div>                
                <ul class="list-group"></ul>
            </div>
        </div>
    </div>
</div>                      


<div class="modal fade" id="editLink_Modal" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit</h4>
            </div>

            <div class="modal-body">
                <input type="text" class="form-control" id="edit_link_title">
                <br>
                <input type="text" class="form-control" id="edit_link_url">
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="edit_link_button" data-dismiss="modal">Save changes</button>
            </div>

        </div>
    </div>
</div>            


<div id="selectedProject_Modal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Selected Project</h4>
            </div>
            <div class="modal-body">           
                <input type="hidden" class="task_ProjectModal_Task_id" value="">
                <input type="hidden" class="edit-task-projects-id" value="">
                <div class="form-group edit-task-projects">
                    <ul>

                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-default pull-right" data-dismiss="modal" id="selectedProject_Modal_ok">SAVE</button>
            </div>
        </div>
    </div>
</div>


<div id="selectedParent_Modal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Selected Parent</h4>
            </div>
            <div class="modal-body">           
                <input type="hidden" class="edit-task-parent-id" value="">
                <div class="form-group edit-task-parent">
                    <ul>

                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-default pull-right" data-dismiss="modal" id="selectedParent_Modal_ok">SAVE</button>
            </div>
        </div>
    </div>
</div>

<div id="subTasks_Modal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Sub-Tasks</h4>
            </div>
            <div class="modal-body">           
                <div class="form-group sub-tasks">
                    <ul>

                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>            


<div class="modal fade del_modal" id="delTask_Modal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Confirm delete task</h4>
            </div>
            <div class="modal-body" style="background-color: red;">
                <input type="hidden" class="delTask_id" value="">
                DELETE TASK : <p></p> ?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-default pull-right" data-dismiss="modal" id="delTask_Modal_Button_c">DEL</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade del_modal" id="delRTaskCurrent_Modal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Confirm delete task - current instance</h4>
            </div>
            <div class="modal-body">
                <input type="hidden" class="delTask_id" value="">
                <h2 style="color : red;">D. Current instance : </h2><p></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-default pull-right" data-dismiss="modal" id="delRTaskCurrent_Modal_c">DEL</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade del_modal" id="delRTaskAll_Modal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Confirm delete task - all instances</h4>
            </div>
            <div class="modal-body">
                <input type="hidden" class="delTask_id" value="">
                <h2 style="background-color: red;">D. All events in the series : </h2><p></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-default pull-right" data-dismiss="modal" id="delRTaskAll_Modal_c">DEL</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="date_time_Modal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>Start:</label>
                    <input class="datepicker-sch" type="text" title="start" />
                    <input class="timepicker-sch" type="text" title="start" />
                </div>    
                <div class="form-group">
                    <label>End:</label>
                    <input class="datepicker-sch" type="text" title="end" />
                    <input class="timepicker-sch" type="text" title="end" />
                </div>        

                <div class="[ form-group ]">
                    <input type="checkbox" name="allDay" id="allDay" autocomplete="off" />
                    <div class="[ btn-group ]">
                        <label for="allDay" class="[ btn btn-default ]">
                            <span class="[ glyphicon glyphicon-ok ]"></span>
                            <span> </span>
                        </label>
                        <label for="allDay" class="[ btn btn-default active ]">
                            All Day
                        </label>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>            


<div class="modal fade" id="repeatTask_Modal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Repeat</h4>
            </div>
            <div class="modal-body">
                <input type="hidden" class="taskRepeat_Modal_Task_id" value="">

                <label for="sel1">Repeats mode:</label>
                <select class="form-control" id="repeat_options">
                    <option value="d">Daily</option>
                    <option value="w">Weekly</option>
                    <option value="m">Monthly</option>
                </select>
                <br>
                <div class="r d">
                    <label>Repeat every:</label>
                    <select id="repeat_days">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                        <option value="13">13</option>
                        <option value="14">14</option>
                        <option value="15">15</option>
                        <option value="16">16</option>
                        <option value="17">17</option>
                        <option value="18">18</option>
                        <option value="19">19</option>
                        <option value="20">20</option>
                        <option value="21">21</option>
                        <option value="22">22</option>
                        <option value="23">23</option>
                        <option value="24">24</option>
                        <option value="25">25</option>
                        <option value="26">26</option>
                        <option value="27">27</option>
                        <option value="28">28</option>
                        <option value="29">29</option>
                        <option value="30">30</option>
                    </select><label>days</label>

                </div>


                <div class="r w">
                    <label>Repeat every:</label>
                    <select id="repeat_weeks">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                        <option value="13">13</option>
                        <option value="14">14</option>
                        <option value="15">15</option>
                        <option value="16">16</option>
                        <option value="17">17</option>
                        <option value="18">18</option>
                        <option value="19">19</option>
                        <option value="20">20</option>
                        <option value="21">21</option>
                        <option value="22">22</option>
                        <option value="23">23</option>
                        <option value="24">24</option>
                        <option value="25">25</option>
                        <option value="26">26</option>
                        <option value="27">27</option>
                        <option value="28">28</option>
                        <option value="29">29</option>
                        <option value="30">30</option>
                    </select>
                    <label>weeks</label>
                    <br>
                    <label>Repeat on:</label>
                    <div>
                        <span class="ep-rec-dow">
                            <input id="dow1" name="MO" type="checkbox" class="dow">
                            <label for="dow1" title="Monday">M</label>
                        </span>
                        <span class="ep-rec-dow">
                            <input id="dow2" name="TU" type="checkbox" class="dow">
                            <label for="dow2" title="Tuesday">T</label>
                        </span>
                        <span class="ep-rec-dow">
                            <input id="dow3" name="WE" type="checkbox" class="dow">
                            <label for="dow3" title="Wednesday">W</label></span>
                        <span class="ep-rec-dow">
                            <input id="dow4" name="TH" type="checkbox" class="dow">
                            <label for="dow4" title="Thursday">T</label>
                        </span>
                        <span class="ep-rec-dow">
                            <input id="dow5" name="FR" type="checkbox" class="dow">
                            <label for="dow5" title="Friday">F</label>
                        </span>
                        <span class="ep-rec-dow">
                            <input id="dow6" name="SA" type="checkbox" class="dow">
                            <label for="dow6" title="Saturday">S</label>
                        </span>
                        <span class="ep-rec-dow">
                            <input id="dow0" name="SU" type="checkbox" class="dow">
                            <label for="dow0" title="Sunday">S</label>
                        </span>
                    </div>                                
                </div>


                <div class="r m">
                    <label>Repeat every:</label>
                    <select id="repeat_months">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                        <option value="13">13</option>
                        <option value="14">14</option>
                        <option value="15">15</option>
                        <option value="16">16</option>
                        <option value="17">17</option>
                        <option value="18">18</option>
                        <option value="19">19</option>
                        <option value="20">20</option>
                        <option value="21">21</option>
                        <option value="22">22</option>
                        <option value="23">23</option>
                        <option value="24">24</option>
                        <option value="25">25</option>
                        <option value="26">26</option>
                        <option value="27">27</option>
                        <option value="28">28</option>
                        <option value="29">29</option>
                        <option value="30">30</option>
                    </select>
                    <label>months</label>
                    <br>
                    <label>Repeat by:</label>
                    <span class="">
                        <input id="domrepeat" name="repeatby" type="radio" value="dm">
                        <label for="domrepeat">day of the month</label>
                    </span>
                    <span class="">
                        <input id="dowrepeat" name="repeatby" type="radio" value="dw">
                        <label for="dowrepeat">day of the week</label>
                    </span>
                </div>


                <br>
                <label id="rstart-label">Starts on:</label>
                <input class="datepicker-sch" title="rstart" data-value="">
                <div class="[ form-group ]">
                    <input type="checkbox" name="allDayRepeat" id="allDayRepeat" autocomplete="off" />
                    <div class="[ btn-group ]">
                        <label for="allDayRepeat" class="[ btn btn-default ]">
                            <span class="[ glyphicon glyphicon-ok ]"></span>
                            <span> </span>
                        </label>
                        <label for="allDayRepeat" class="[ btn btn-default active ]">
                            All Day
                        </label>
                    </div>
                    <input class="timepicker-sch" type="text" title="startRepeat" />
                    <input class="timepicker-sch" type="text" title="endRepeat" />
                </div>
                <br>
                <label class="ep-rec-ends-th">Ends:</label>
                <span class="ep-rec-ends-opt">
                    <input id="endson_never" name="endson" type="radio" value="never">
                    Never
                </span>
                <br>
                <span class="ep-rec-ends-opt">
                    <input id="endson_count" name="endson" type="radio" value="after">
                    After <input id="endson_count_input" size="3" value=""> occurrences
                </span>
                <br>
                <span class="ep-rec-ends-opt">
                    <input id="endson_until" name="endson" type="radio" value="on_date">
                    On <input class="datepicker-sch" title="endson_until_input" data-value="">
                </span>


            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-default pull-right" data-dismiss="modal" id="repeatTask_Modal_done">DONE</button>
            </div>
        </div>

    </div>
</div>

<jsp:include page="errorModal.jsp" />