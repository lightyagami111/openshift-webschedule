<%-- 
    Document   : taskForm
    Created on : Aug 15, 2017, 1:39:55 PM
    Author     : ivaylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<span id="task_buttons">
    <button type="button" class="btn btn-sm btn-default makeEditableTaskInfo">EDIT</button>
    <button type="button" class="btn btn-sm btn-default makeEditableCurrentTaskInfo">EDIT Current</button>
    <button type="button" class="btn btn-sm btn-info delTask_Modal_Button" data-toggle="modal" data-target="#delTask_Modal">DELETE Task</button>    
    <button type="button" class="btn btn-sm btn-info delRTaskCurrent_Modal_Button" data-toggle="modal" data-target="#delRTaskCurrent_Modal">DELETE Current</button>    
    <button type="button" class="btn btn-sm btn-info delRTaskAll_Modal_Button" data-toggle="modal" data-target="#delRTaskAll_Modal">DELETE Repeat Data</button>    
    <br><br>
</span>
<form class="editTask">
    <fieldset disabled="disabled">                                    
        <input type="hidden" class="task_Modal_Task_id" value="">
        <input type="hidden" class="fromRepeatTask_Modal_Task_id" value="">
        <input type="hidden" class="fromRepeatTask_Modal_Task_start" value="">
        <div class="form-group">
            <button type="button" class="btn btn-default" data-toggle="modal" data-target="#selectedProject_Modal">Selected project : <span class="selectedProject_span"></span></button>
            <button type="button" class="btn btn-default" data-toggle="modal" data-target="#selectedParent_Modal">Selected parent : <span class="selectedParent_span"></span></button>
            <button type="button" class="btn btn-success" data-toggle="modal" data-target="#subTasks_Modal">sub-tasks</button>
        </div>  
        <div class="form-group">
            <label>Name:</label>
            <input type="text" name="taskName" autofocus class="form-control">
        </div>                                      


        <div class="[ form-group ]">
            <button type="button" class="btn btn-primary date_time_Modal_Button" data-toggle="modal" data-target="#date_time_Modal">Date & Time</button>
            <button type="button" class="btn btn-primary repeatTask_Modal_Button" data-toggle="modal" data-target="#repeatTask_Modal">Repeat</button>
        </div>

        <br>
        <div class="form-group">
            <label>Calendar : </label>
            <select class="task_cal"></select>
        </div> 
        <br>
        <div class="form-group">
            <label>Priority : </label>
            <select class="task_prio">
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
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-default editNotes_Modal_Button" data-toggle="modal" data-target="#editNotes_Modal">Notes</button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-default editLinks_Modal_Button" data-toggle="modal" data-target="#editLinks_Modal">Links</button>
        </div>     
        <div class="form-group labels">
            <label for="sel1">Select label:</label>
            <input class="labels-sch" type="text" value="" />     
        </div>             

        <br>
        <button class="btn btn-default pull-right saveSchData">SAVE</button>
    </fieldset>
</form>