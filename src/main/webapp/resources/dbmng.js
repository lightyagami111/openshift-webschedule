
var _csrf = null;

$(document).ready(function () {
    _csrf = '?' + $('#_csrf').attr('name') + '=' + $('#_csrf').val();
});

function errorMessage(xhr) {
    $('#error_Modal').find('.modal-title').text('status = ' + xhr.status + '; status text = ' + xhr.statusText);
    $('#error_Modal').find('.modal-body').html(xhr.responseText);
    $('#error_Modal_Button').click();
}

function saveLink(body, callback) {
    jQuery.ajax({
        type: "POST",
        url: '/WebSchedule/link' + _csrf,
        data: JSON.stringify(body),
        contentType: 'application/json; charset=UTF-8',
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function deleteLink(link, task, callback) {
    jQuery.ajax({
        type: "DELETE",
        url: '/WebSchedule/link' + _csrf + "&link=" + link + "&task=" + task,
        success: function () {
            callback();
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}


function updateLink(link, callback) {
    jQuery.ajax({
        type: "PUT",
        url: '/WebSchedule/link' + _csrf,
        data: JSON.stringify(link),
        contentType: 'application/json; charset=UTF-8',
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
        success: function (result) {
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
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function loadInitialTaskData(project_id, parent_id, selectedStart, selectedEnd, insert, labels, callback) {
    var sParentid = "";
    if (parent_id !== null) {
        if (parent_id === '#') {
            parent_id = '%23';
        }
        sParentid = "parent_id=" + parent_id;
    }

    var sProject = "&project_id=" + project_id;
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
        data: sParentid + sStart + sEnd + sProject + sInsert + sLabels,
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
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}


function loadTaskRepeatData(task_id, callback) {
    var sTaskId = "";
    if (task_id !== null) {
        sTaskId = "id=" + task_id;
    }

    jQuery.ajax({
        type: "GET",
        url: '/WebSchedule/loadTaskRepeatData',
        data: sTaskId,
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}

function saveTaskRepeatData(repeatData, callback) {
    jQuery.ajax({
        type: "POST",
        url: '/WebSchedule/saveTaskRepeatData' + _csrf,
        data: JSON.stringify(repeatData),
        contentType: 'application/json; charset=UTF-8',
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


function addNewProject(pdata, callback) {
    jQuery.ajax({
        type: "POST",
        url: '/WebSchedule/addNewProject' + _csrf,
        data: JSON.stringify(pdata),
        contentType: 'application/json; charset=UTF-8',
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
        success: function (result) {
            callback(result);
        },
        error: function (xhr) {
            errorMessage(xhr);
        }
    });
}