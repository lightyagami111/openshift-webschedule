
/* global $c */

var _csrf = null;

$(document).ready(function () {
    _csrf = '?' + $('#_csrf').attr('name') + '=' + $('#_csrf').val();
});

function errorMessage(xhr) {
    if (xhr.responseText.indexOf("action='/WebSchedule/login'")) {
        window.location.href = '/WebSchedule/login';
    }
    $('#error_Modal').find('.modal-title').text('status = ' + xhr.status + '; status text = ' + xhr.statusText);
    $('#error_Modal').find('.modal-body').html(xhr.responseText);
    $('#error_Modal_Button').click();
}

function getTitleFromUrlLink(body, callback) {
    jQuery.ajax({
        type: "POST",
        url: '/WebSchedule/link/title' + _csrf,
        data: JSON.stringify(body),
        contentType: 'application/json; charset=UTF-8',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}






function loadCalendars(callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadCalendars',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function getCalendarByid(calendar_id, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/getCalendarByid',
        processData: false,
        data: "id=" + calendar_id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function addNewCalendar(pdata, callback) {
    jQuery.ajax({
        type: "POST",
        url: '/WebSchedule/addNewCalendar' + _csrf,
        data: JSON.stringify(pdata),
        contentType: 'application/json; charset=UTF-8',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function updateCalendar(pdata, callback) {
    jQuery.ajax({
        type: "PUT",
        url: '/WebSchedule/updateCalendar' + _csrf,
        data: JSON.stringify(pdata),
        contentType: 'application/json; charset=UTF-8',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function deleteCalendar(id, callback) {
    jQuery.ajax({
        type: "DELETE",
        url: '/WebSchedule/deleteCalendar' + _csrf + "&id=" + id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}


function getDefaultCalendar(callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/getDefaultCalendar',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}


function setDefaultCalendar(calendar_id, callback) {
    jQuery.ajax({
        type: "PUT",
        url: '/WebSchedule/setDefaultCalendar' + _csrf + "&id=" + calendar_id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function setSelectedCalendar(selectedC, callback) {
    var selectedC_p = '';
    for(var i=0; i<selectedC.length; i++) {
        selectedC_p = selectedC_p + '&ids=' + selectedC[i];
    }
    
    jQuery.ajax({
        type: "PUT",
        url: '/WebSchedule/setSelectedCalendar' + _csrf + selectedC_p,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}



function loadEvents(start, end, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadEvents',
        data: "start=" + start + "&end=" + end,
        dataType: "json",
        success: function (result) {
            for(var t=0; t<result.length; t++){
                result[t].textColor = $c.complement(result[t].color);
            }
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}


function loadTasksByProject(project_id, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadTasksByProject',
        data: "id=" + project_id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function loadTasksByProjectByGroup(project_id, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadTasksByProject/group',
        data: "id=" + project_id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function loadTasksByLabel(label_id, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadTasksByLabel',
        data: "id=" + label_id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function loadTasksBySearch(searchTerm, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadTasksBySearch',
        data: "searchTerm=" + encodeURIComponent(searchTerm),
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}




function getDefaultProject(callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/getDefaultProject',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}


function setDefaultProject(project_id, callback) {
    jQuery.ajax({
        type: "PUT",
        url: '/WebSchedule/setDefaultProject' + _csrf + "&id=" + project_id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}



function loadTaskData(task_id, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadTaskData',
        data: "id=" + task_id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function getParentTaskProject(parent_id, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/getParentTaskProject',
        data: "parent_id=" + parent_id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function loadInitialTaskData(calendar_id, project_id, parent_id, selectedStart, selectedEnd, insert, labels, callback) {
    var sParentid = "";
    if (parent_id !== null) {
        if (parent_id === '#') {
            parent_id = '%23';
        }
        sParentid = "parent_id=" + parent_id;
    }

    var sProject = "&project_id=" + project_id;
    var sCal = "&calendar_id=" + calendar_id;
    var sInsert = "&insert=" + insert;

    var sLabels = "";
    if (labels !== null) {
        for (var i = 0; i < labels.length; i++) {
            sLabels = sLabels + "&labels=" + labels[i];
        }
    }

    var sStart = "";
    if (selectedStart !== null) {
        sStart = "&start=" + selectedStart;
    }

    var sEnd = "";
    if (selectedEnd !== null) {
        sEnd = "&end=" + selectedEnd;
    }



    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadInitialTaskData',
        data: sParentid + sStart + sEnd + sProject + sCal + sInsert + sLabels,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function saveTaskData(taskData, callback) {
    jQuery.ajax({
        type: "POST",
        url: '/WebSchedule/saveTaskData' + _csrf,
        data: JSON.stringify(taskData),
        contentType: 'application/json; charset=UTF-8',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function updateTaskTitle(id, new_title, callback) {
    jQuery.ajax({
        type: "PUT",
        url: '/WebSchedule/updateTaskTitle' + _csrf + "&id=" + id + "&new_title=" + encodeURIComponent(new_title),
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function deleteTaskData(id, callback) {
    jQuery.ajax({
        type: "DELETE",
        url: '/WebSchedule/deleteTaskData' + _csrf + "&id=" + id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}
function deleteRCurrentTaskData(id, date, callback) {
    jQuery.ajax({
        type: "DELETE",
        url: '/WebSchedule/deleteRCurrentTaskData' + _csrf + "&id=" + id + "&date=" + date,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}
function deleteRAllTaskData(id, callback) {
    jQuery.ajax({
        type: "DELETE",
        url: '/WebSchedule/deleteRAllTaskData' + _csrf + "&id=" + id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}



function loadTaskRepeatDataCurrentEvent(task_id, start, end, callback) {
    var params = "id=" + task_id + "&start=" + start;
    if (end !== null) {
        params = params + "&end=" + end;
    }
    
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadTaskRepeatDataCurrentEvent',
        data: params,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}




function loadProjects(callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadProjects',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function getProjectByid(project_id, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/getProjectByid',
        processData: false,
        data: "id=" + project_id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function getProjectByid_loaded(pps, project_id) {
    var p = null;
    for (var i = 0; i < pps.length; i++) {
        if (pps[i].id == project_id) {
            p = pps[i];
        }
    }
    return p;
}

function getTaskById_loaded(task_list, task_id) {
    var p = null;
    for (var i = 0; i < task_list.length; i++) {
        if (task_list[i].id == task_id) {
            p = task_list[i];
        }
    }
    return p;
}

function addNewProject(pdata, callback) {
    jQuery.ajax({
        type: "POST",
        url: '/WebSchedule/addNewProject' + _csrf,
        data: JSON.stringify(pdata),
        contentType: 'application/json; charset=UTF-8',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function updateProject(pdata, callback) {
    jQuery.ajax({
        type: "PUT",
        url: '/WebSchedule/updateProject' + _csrf,
        data: JSON.stringify(pdata),
        contentType: 'application/json; charset=UTF-8',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}
function deleteProject(id, callback) {
    jQuery.ajax({
        type: "DELETE",
        url: '/WebSchedule/deleteProject' + _csrf + "&id=" + id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}




function loadLabels(callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadLabels',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function getLabelById(lid, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/getLabelByid',
        processData: false,
        data: "id=" + lid,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function addNewLabel(pdata, callback) {
    jQuery.ajax({
        type: "POST",
        url: '/WebSchedule/addNewLabel' + _csrf,
        data: JSON.stringify(pdata),
        contentType: 'application/json; charset=UTF-8',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function updateLabel(pdata, callback) {
    jQuery.ajax({
        type: "PUT",
        url: '/WebSchedule/updateLabel' + _csrf,
        data: JSON.stringify(pdata),
        contentType: 'application/json; charset=UTF-8',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function deleteLabel(id, callback) {
    jQuery.ajax({
        type: "DELETE",
        url: '/WebSchedule/deleteLabel' + _csrf + "&id=" + id,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function saveOrUpdateGS(gs_data, callback) {
    jQuery.ajax({
        type: "POST",
        url: '/WebSchedule/gs' + _csrf,
        data: JSON.stringify(gs_data),
        contentType: 'application/json; charset=UTF-8',
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function findGS(view, value, callback) {
    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/gs',
        processData: false,
        accept: "application/json; charset=utf-8",         
        contentType: "application/json; charset=utf-8"  ,
        data: "selectedView=" + view + "&selectedId=" + value,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}