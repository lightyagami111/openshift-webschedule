var textboxioNotes;
var selectedCalEvent;

$(document).ready(function () {

    loadProjects(function (data_projects) {
        loadProjectsMenu(data_projects);
        loadEditTaskProjects(data_projects);
    });

    loadLabels(function (result) {
        loadLabelsAjaxCallback(result);
    });




    //-------------------------------------
    //load calendars
    //-------------------------------------
    var h = {
        left: 'prev,next today',
        center: 'title',
        right: 'month,basicWeek,basicDay'
    };
    if (mobileCheck() === true) {
        h = {
            left: 'prev,next today month,basicWeek,basicDay title',
            center: '',
            right: 'title'
        };
    }
    $('#calendar').fullCalendar({
        header: h,
        views: {
            week: {
                titleFormat: 'MMMM DD YYYY',
                columnFormat: 'MMM DD ddd'
            },
            month: {
                titleFormat: 'MMMM YYYY'
            }
        },
        defaultView: 'basicWeek',
        timeFormat: 'HH:mm',
        displayEventEnd: true,
        aspectRatio: mobileCheck() ? 1 : 1.35,
        firstDay: 1,
        selectable: true,
        selectHelper: true,
        select: function (start, end) {
            var startD = start.local().toDate();
            var endD = null;
            if (end.diff(start, 'days') > 1) {
                end.subtract(1, 'days');
                endD = end.local().toDate();
                endD = moment(endD).format('YYYY-MM-DD');
            }
            startD = moment(startD).format('YYYY-MM-DD');

            showTaskInfoModal(null, startD, endD, false);
        },
        eventClick: function (calEvent, jsEvent, view) {
            selectedCalEvent = calEvent;
            showTaskInfoModal(calEvent.id, null, null, true);
        },
        events: function (start, end, timezone, callback) {
            var startD = start.local().format('YYYY-MM-DD');
            var endD = end.local().format('YYYY-MM-DD');

            $('input[name="selectedView"]').val('calendar');
            $('input[name="selectedValue"]').val(startD + ' - ' + endD);

            loadEvents(startD, endD, function (result) {
                callback(result);
            });
        }
    });
    if (mobileCheck() === true) {
        $('.fc-left h2').before('<br>');
    }

    $('#showCalendar').on('click', showCalendarWrapper);


    //-------------------------------------
    // edit task
    //-------------------------------------
    loadTaskParent();

    $('.sub-tasks').jstree({
        "core": {
            'themes': {
                'name': 'proton',
                'responsive': true
            },
            "check_callback": true
        }
    });

    initDateTimePickers();

    textboxioNotes = textboxio.replace('#editNotes_Modal .textarea-sch');


    $('.makeEditableTaskInfo').on('click', function () {
        var form = $(this).parents('.fbody');
        enableTaskEditing(form);
    });
    $('.makeEditableCurrentTaskInfo').on('click', function () {
        var form = $(this).parents('.fbody');
        var id = form.find('.task_Modal_Task_id').val();
        var start = selectedCalEvent.start._i;
        var end = null;
        if (selectedCalEvent.end !== null) {
            end = selectedCalEvent.end._i;
        }

        loadTaskRepeatDataCurrentEvent(id, start, end, function (result) {
            var form = $('#formBodyModal');
            enableTaskEditing(form);
            loadTaskRepeatData(result.taskData.id, function (repeatData) {
                loadSchDataAjaxCallback(form, result.taskData, repeatData, result.task_id, result.repeatTaskStart);
            });
        });

    });
    $('.saveSchData').on('click', function (event) {
        event.preventDefault();
        var form = $(this).parents('.fbody');
        saveSchData(form);
    });

    $("#delTask_Modal_Button_c").on('click', function () {
        var tid = $('.delTask_id').val();
        deleteTaskData(tid, function () {
            $('#editTask_Modal').modal('hide');
            refreshData();
        });
    });
    $("#delRTaskCurrent_Modal_c").on('click', function () {
        var tid = $('.delTask_id').val();
        var date = selectedCalEvent.start._i;
        deleteRCurrentTaskData(tid, date, function () {
            $('#editTask_Modal').modal('hide');
            refreshData();
        });
    });
    $("#delRTaskAll_Modal_c").on('click', function () {
        var tid = $('.delTask_id').val();
        deleteRAllTaskData(tid, function () {
            $('#editTask_Modal').modal('hide');
            refreshData();
        });
    });

    $('#formBodyDesktop').find('input[name="repeatTrue"]').attr('id', 'repeatTrue_d');
    $('#formBodyDesktop').find('label[for="repeatTrue"]').attr('for', 'repeatTrue_d');
    $('#formBodyModal').find('input[name="repeatTrue"]').attr('id', 'repeatTrue_m');
    $('#formBodyModal').find('label[for="repeatTrue"]').attr('for', 'repeatTrue_m');
    


    //-------------------------------------
    // repeatTask_Modal
    //-------------------------------------
    $('#repeat_options option').on('click', function () {
        $('.r').hide();
        $('.' + $(this).val()).show();
    });

    $('#endson_never').on('click', function () {
        $('#endson_count_input').prop('disabled', true);
        setDatePickerPropDisabled('endson_until_input', true);
    });
    $('#endson_count').on('click', function () {
        $('#endson_count_input').prop('disabled', false);
        setDatePickerPropDisabled('endson_until_input', true);
    });
    $('#endson_until').on('click', function () {
        $('#endson_count_input').prop('disabled', true);
        setDatePickerPropDisabled('endson_until_input', false);
    });
    $('.repeatTask_Modal_Button').on('click', function () {
        var form = $(this).parents('.fbody');
        var tid = form.find('.task_Modal_Task_id').val();
        loadRepeatSchData(tid);
    });
    $('#repeatTask_Modal_done').on('click', function (event) {
        event.preventDefault();
        saveRepeatSchData();
    });


    //-------------------------------------
    // GROUP && SORT 
    //-------------------------------------
    loadGroupAndSort();








    hideTasksWrapper();
    hideTaskInfo();
    hideCalendarWrapper();

    $('#showCalendar').click();
    $('#showCalendar').removeClass('active');

    if (mobileCheck() === false) {
        $('#page-wrapper').css({
            'margin': '0 0 0 400px'
        });
    }



});











$('.modal').on('hidden.bs.modal', function (e) {
    if ($('.modal').hasClass('in')) {
        $('body').addClass('modal-open');
    }
});




function showCalendarWrapper() {
    $('#calendar-wrapper').show(250);
    $('#calendar').show(250);

    hideTasksWrapper();
    deselectProjectMenu();

    var view = $('#calendar').fullCalendar('getView');
    var startD = view.start.local().format('YYYY-MM-DD');
    var endD = view.end.local().format('YYYY-MM-DD');
    $('input[name="selectedView"]').val('calendar');
    $('input[name="selectedValue"]').val(startD + ' - ' + endD);
    $('#calendar').fullCalendar('option', 'contentHeight', "auto");

    refreshData();
}

function hideCalendarWrapper() {
    $('#calendar-wrapper').hide();
}

function showTasksWrapper() {
    $('#tasks-wrapper').show(250);
}

function hideTasksWrapper() {
    $('#tasks-wrapper').hide();
}

function hideTaskInfo() {
    $('#ts_info').hide();
    $('#editTask_Modal').modal('hide');
}

function showTaskInfoMobileCheck(task_id) {

    var form = null;
    if (mobileCheck() === false) {
        form = $('#formBodyDesktop');
        $('#ts_info').show(250);
    } else {
        form = $('#formBodyModal');
        $('#editTask_Modal_Button').click();
    }

    disableTaskEditing(form);

    loadSchData(form, task_id, null, null);

}

function showTaskInfoModal(task_id, start, end, disableForm) {
    var form = $('#formBodyModal');
    $('#editTask_Modal_Button').click();

    if (disableForm) {
        disableTaskEditing(form);
    } else {
        enableTaskEditing(form);
    }

    loadSchData(form, task_id, start, end);
}






function enableTaskEditing(form) {
    form.find('form.editTask fieldset').removeAttr('disabled');
    var selectizeLabels = getSelectizeLabels(form);
    selectizeLabels.enable();
}


function disableTaskEditing(form) {
    form.find('form.editTask fieldset').attr('disabled', 'disabled');
    var selectizeLabels = getSelectizeLabels(form);
    selectizeLabels.clear();
    selectizeLabels.disable();
}













//-------------------------------------------------------
//           LOAD AND SAVE DATA
//-------------------------------------------------------

function loadSchData(form, task_id, start, end) {
    if (task_id !== null) {
        loadTaskData(task_id, function (taskData) {
            loadTaskRepeatData(taskData.id, function (repeatData) {
                loadSchDataAjaxCallback(form, taskData, repeatData, null, null);
            });
        });
    } else {
        getDefaultProject(function (result) {
            loadInitialTaskData(result.project_id, '#', start, end, false, null, function (taskData) {
                loadTaskRepeatData(null, function (repeatData) {
                    loadSchDataAjaxCallback(form, taskData, repeatData, null, null);
                });
            });
        });
    }

}


function loadSchDataAjaxCallback(form, taskData, repeatData, fromRepeatTaskId, fromRepeatTaskStart) {


    if (repeatData.dbExist === true) {
        form.find('.repeatTask_Modal_Button').removeClass('btn-default');
        form.find('.repeatTask_Modal_Button').addClass('btn-primary');
    } else {
        form.find('.repeatTask_Modal_Button').removeClass('btn-primary');
        form.find('.repeatTask_Modal_Button').addClass('btn-default');
    }
    form.find('input[name="repeatTrue"]').prop('checked', repeatData.do_repeat);


    if ($('input[name="selectedView"]').val() === 'calendar' && repeatData.dbExist === true) {
        form.find('.makeEditableCurrentTaskInfo').show();
    } else {
        form.find('.makeEditableCurrentTaskInfo').hide();
    }
    if (repeatData.dbExist === true) {
        form.find('.delRTaskAll_Modal_Button').show();
    }
    else {
        form.find('.delRTaskAll_Modal_Button').hide();
    }
    if ($('input[name="selectedView"]').val() === 'calendar') {
        form.find('.delRTaskCurrent_Modal_Button').show();
    } else {
        form.find('.delRTaskCurrent_Modal_Button').hide();
    }

    if (taskData.id === null && $('input[name="selectedView"]').val() === 'calendar') {
        form.find('#task_buttons').hide();
    } else {
        form.find('#task_buttons').show();
        $(".del_modal").find(".modal-body p").text(taskData.text);
        $('.delTask_id').val(taskData.id);
    }



    form.find('.task_Modal_Task_id').val(taskData.id);
    form.find('.fromRepeatTask_Modal_Task_id').val(fromRepeatTaskId);
    form.find('.fromRepeatTask_Modal_Task_start').val(fromRepeatTaskStart);
    form.find('input[name="taskName"]').val(taskData.text);



    if (taskData.start !== null || taskData.end !== null) {
        form.find('.date_time_Modal_Button').removeClass('btn-default');
        form.find('.date_time_Modal_Button').addClass('btn-primary');
    } else {
        form.find('.date_time_Modal_Button').removeClass('btn-primary');
        form.find('.date_time_Modal_Button').addClass('btn-default');
    }
    $('input[name="allDay"]').prop('checked', taskData.allDay);
    setDateToPicker('start', taskData.start);
    setDateToPicker('end', taskData.end);
    if (taskData.allDay === false) {
        setTimeToPicker('start', taskData.start);
        setTimeToPicker('end', taskData.end);
    } else {
        setTimeToPicker('start', null);
        setTimeToPicker('end', null);
    }




    setEditTaskProject(taskData.project_id);
    $('.task_ProjectModal_Task_id').val(taskData.id);
    form.find('.selectedProject_span').text(taskData.project_text);



    changeEditTaskParent(taskData.project_id, taskData.parent, taskData.id, taskData.parent_text);



    var selectizeLabels = getSelectizeLabels(form);
    if (taskData.labels !== null) {
        for (var i = 0; i < taskData.labels.length; i++) {
            selectizeLabels.addItem(taskData.labels[i].id, true);
        }
    }



    form.find('.task_prio').val(taskData.priority);



    $('#link_input').val('');
    if (taskData.links !== null && taskData.links.length > 0) {
        loadLinksList(taskData.links, taskData.id);
        form.find('.editLinks_Modal_Button').removeClass('btn-default');
        form.find('.editLinks_Modal_Button').addClass('btn-primary');
    } else {
        loadLinksList([], taskData.id);
        form.find('.editLinks_Modal_Button').removeClass('btn-primary');
        form.find('.editLinks_Modal_Button').addClass('btn-default');
    }



    var jnotes = $('<div></div>').html(taskData.notes);
    if (taskData.notes !== null && jnotes.text().replace(/\s/g, '').length > 0) {
        textboxioNotes.content.set(taskData.notes);
        form.find('.editNotes_Modal_Button').removeClass('btn-default');
        form.find('.editNotes_Modal_Button').addClass('btn-primary');
    } else {
        textboxioNotes.content.set('');
        form.find('.editNotes_Modal_Button').removeClass('btn-primary');
        form.find('.editNotes_Modal_Button').addClass('btn-default');
    }



    var btnSubTasks = form.find('button[data-target="#subTasks_Modal"]');
    if (taskData.subTasks !== null && taskData.subTasks.length > 0) {
        btnSubTasks.removeClass('btn-default');
        btnSubTasks.addClass('btn-success');
    } else {
        btnSubTasks.removeClass('btn-success');
        btnSubTasks.addClass('btn-default');
    }
    var jsTreeInst = $('.sub-tasks').jstree(true);
    jsTreeInst.settings.core.data = taskData.subTasks;
    jsTreeInst.refresh();
    $('.sub-tasks').on('refresh.jstree', function (e, data) {
        $('.sub-tasks').jstree("open_all");
    });


}


function saveSchData(form) {
    var taskData = new Object();

    var tid = form.find('.task_Modal_Task_id').val();
    if (tid.length > 0) {
        taskData.id = tid;
    }

    var ftid = form.find('.fromRepeatTask_Modal_Task_id').val();
    if (ftid.length > 0) {
        taskData.fromRepeatTaskId = ftid;
    }
    var ftstart = form.find('.fromRepeatTask_Modal_Task_start').val();
    if (ftstart.length > 0) {
        taskData.fromRepeatTaskStart = ftstart;
    }

    var title = form.find('input[name="taskName"]').val();
    if (title.length === 0) {
        hideTaskInfo();
        return;
    }
    taskData.text = title;

    taskData.allDay = $('input[name="allDay"]').prop('checked');
    if (taskData.allDay === true) {
        setTimeToPicker('start', null);
        setTimeToPicker('end', null);
    }
    taskData.start = getDateTimeData('start');
    taskData.end = getDateTimeData('end');

    taskData.project_id = getEditTaskProject();
    taskData.parent = getTaskParent();
    taskData.parent_text = getTaskParentText(form);

    taskData.labels = null;
    var selectizeLabels = getSelectizeLabels(form);
    if (selectizeLabels.items.length > 0) {
        taskData.labels = selectizeLabels.items;
    }

    taskData.priority = form.find('.task_prio').val();

    var content = textboxioNotes.content.get();
    taskData.notes = content;

    taskData.do_repeat = form.find('input[name="repeatTrue"]').prop('checked');

    saveTaskData(taskData, function () {
        refreshData();
    });
}


function loadRepeatSchData(task_id) {
    loadTaskRepeatData(task_id, loadRepeatSchDataAjaxCallback);
}

function loadRepeatSchDataAjaxCallback(repeatData) {
    $('.taskRepeat_Modal_Task_id').val(repeatData.task_id);
    $('#repeatTrue').prop('checked', repeatData.do_repeat);
    $('.r').hide();
    $('.' + repeatData.mode).show();
    $('#repeat_options').val(repeatData.mode);
    $('input[name="endson"][value="' + repeatData.endson + '"]').click();
    $('#endson_count_input').val(repeatData.endson_count);

    setDateToPicker('rstart', repeatData.mode_start);
    $('input[name="allDayRepeat"]').prop('checked', repeatData.allDay);
    if (repeatData.allDay === false) {
        setTimeToPicker('startRepeat', repeatData.mode_start);
        setTimeToPicker('endRepeat', repeatData.mode_end);
    } else {
        setTimeToPicker('startRepeat', null);
        setTimeToPicker('endRepeat', null);
    }


    setDateToPicker('endson_until_input', repeatData.endson_until);

    $('#repeat_days').val(repeatData.repeat_days);

    $('#repeat_weeks').val(repeatData.repeat_weeks);
    $('.dow').prop('checked', false);
    if (repeatData.repeat_wdays !== null) {
        for (var i = 0; i < repeatData.repeat_wdays.length; i++) {
            $('.dow[name="' + repeatData.repeat_wdays[i] + '"]').prop('checked', true);
        }
    }

    $('#repeat_months').val(repeatData.repeat_months);
    $('input[name="repeatby"]').prop('checked', false);
    if (repeatData.repeatby !== null) {
        $('input[name="repeatby"][value="' + repeatData.repeatby + '"]').prop('checked', true);
    }

    $('#repeat_years').val(repeatData.repeat_years);
}


function saveRepeatSchData() {
    var repeatData = new Object();
    var tid = $('.taskRepeat_Modal_Task_id').val();
    if (tid.length === 0) {
        alert('Please save task data before saving the repaeat data!');
        return;
    }
    repeatData.task_id = tid;
    repeatData.do_repeat = $('#repeatTrue').prop('checked');

    var mode = null;
    if ($('.d').css('display') !== 'none') {
        mode = 'd';
    }
    if ($('.w').css('display') !== 'none') {
        mode = 'w';
    }
    if ($('.m').css('display') !== 'none') {
        mode = 'm';
    }

    repeatData.mode = mode;

    repeatData.endson = $('input[name=endson]:checked').val();

    repeatData.endson_count = $('#endson_count_input').val();
    if (repeatData.endson_count.length === 0) {
        repeatData.endson_count = null;
    }


    repeatData.allDay = $('input[name="allDayRepeat"]').prop('checked');
    if (repeatData.allDay === true) {
        setTimeToPicker('startRepeat', null);
        setTimeToPicker('endRepeat', null);
    }
    repeatData.mode_start = getDateTimeData('rstart', 'startRepeat');
    repeatData.mode_end = getDateTimeData('rstart', 'endRepeat');



    repeatData.endson_until = getDateFromPicker('endson_until_input');
    if (repeatData.endson_until.length === 0) {
        repeatData.endson_until = null;
    }

    repeatData.repeat_days = $('#repeat_days').val();

    repeatData.repeat_weeks = $('#repeat_weeks').val();
    repeatData.repeat_wdays = $('input:checkbox:checked.dow').map(function () {
        return $(this).attr('name');
    }).get();

    repeatData.repeat_months = $('#repeat_months').val();
    repeatData.repeatby = $('input[name=repeatby]:checked').val();

    repeatData.repeat_years = $('#repeat_years').val();

    saveTaskRepeatData(repeatData, function () {
        $('input[name="repeatTrue"]').prop('checked', repeatData.do_repeat);
        $('.repeatTask_Modal_Button').removeClass('btn-default');
        $('.repeatTask_Modal_Button').addClass('btn-primary');
    });
}
