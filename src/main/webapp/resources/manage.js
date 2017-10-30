

/* global DEFAULT_COLOR */

var optionColors = {
    color: DEFAULT_COLOR,
    showPaletteOnly: true,
    hideAfterPaletteSelect: true,
    palette: [
        ["rgb(0, 0, 0)", "rgb(67, 67, 67)", "rgb(102, 102, 102)",
            "rgb(204, 204, 204)", "rgb(217, 217, 217)", "rgb(255, 255, 255)"],
        ["rgb(152, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 153, 0)", "rgb(255, 255, 0)", "rgb(0, 255, 0)",
            "rgb(0, 255, 255)", "rgb(74, 134, 232)", "rgb(0, 0, 255)", "rgb(153, 0, 255)", "rgb(255, 0, 255)"],
        ["rgb(230, 184, 175)", "rgb(244, 204, 204)", "rgb(252, 229, 205)", "rgb(255, 242, 204)", "rgb(217, 234, 211)",
            "rgb(208, 224, 227)", "rgb(201, 218, 248)", "rgb(207, 226, 243)", "rgb(217, 210, 233)", "rgb(234, 209, 220)",
            "rgb(221, 126, 107)", "rgb(234, 153, 153)", "rgb(249, 203, 156)", "rgb(255, 229, 153)", "rgb(182, 215, 168)",
            "rgb(162, 196, 201)", "rgb(164, 194, 244)", "rgb(159, 197, 232)", "rgb(180, 167, 214)", "rgb(213, 166, 189)",
            "rgb(204, 65, 37)", "rgb(224, 102, 102)", "rgb(246, 178, 107)", "rgb(255, 217, 102)", "rgb(147, 196, 125)",
            "rgb(118, 165, 175)", "rgb(109, 158, 235)", "rgb(111, 168, 220)", "rgb(142, 124, 195)", "rgb(194, 123, 160)",
            "rgb(166, 28, 0)", "rgb(204, 0, 0)", "rgb(230, 145, 56)", "rgb(241, 194, 50)", "rgb(106, 168, 79)",
            "rgb(69, 129, 142)", "rgb(60, 120, 216)", "rgb(61, 133, 198)", "rgb(103, 78, 167)", "rgb(166, 77, 121)",
            "rgb(91, 15, 0)", "rgb(102, 0, 0)", "rgb(120, 63, 4)", "rgb(127, 96, 0)", "rgb(39, 78, 19)",
            "rgb(12, 52, 61)", "rgb(28, 69, 135)", "rgb(7, 55, 99)", "rgb(32, 18, 77)", "rgb(76, 17, 48)"]
    ]
};




$(document).ready(function () {

    loadProjects(loadProjectsAjaxCallback2);
    loadLabels(loadLabelsAjaxCallback2);
    loadCalendars(loadCalendarsAjaxCallback2);



    $("#addNewSchCalendar_pcolor").spectrum(optionColors);
    $("#updateSchCalendar_pcolor").spectrum(optionColors);



    $('#addNewProject_Modal_c').on('click', function () {
        addNewSchProject();        
    });

    $('#addNewSchLabel_Modal_c').on('click', function () {
        addNewSchLabel();        
    });    
    
    $('#addNewCalendar_Modal_c').on('click', function () {
        addNewSchCalendar();        
    });
    


    $('#updateProject_Modal_c').on('click', function () {
        updateSchProject();        
    });

    $('#updateSchLabel_Modal_c').on('click', function () {
        updateSchLabel();        
    });
    
    $('#updateCalendar_Modal_c').on('click', function () {
        updateSchCalendar();        
    });
    
    
    

    $('#deleteProject_Modal_c').on('click', function () {
        deleteSchProject();        
    });
    
    $('#deleteSchLabel_Modal_c').on('click', function () {
        deleteSchLabel();        
    });

    $('#deleteCalendar_Modal_c').on('click', function () {
        deleteSchCalendar();
    });





    onHideNewSchLabelModel();
    onHideUpdateSchLabelModal();
    onHideDeleteSchLabelModal();
    
    onHideNewProjectModal();
    onHideUpdateSchProjectModal();
    onHideDeleteSchProjectModal();
    
    onHideNewCalendarModal();
    onHideUpdateSchCalendarModal();
    onHideDeleteSchCalendarModal();
    
    

    $('#addNewSchLabel_Modal').on('hidden.bs.modal', function () {
        onHideNewSchLabelModel();
    });
    $('#updateSchLabel_Modal').on('hidden.bs.modal', function () {
        onHideUpdateSchLabelModal();
    });
    $('#deleteSchLabel_Modal').on('hidden.bs.modal', function () {
        onHideDeleteSchLabelModal();
    });

    $('#addNewProject_Modal').on('hidden.bs.modal', function () {
        onHideNewProjectModal();
    });
    $('#updateProject_Modal').on('hidden.bs.modal', function () {
        onHideUpdateSchProjectModal();
    });
    $('#deleteProject_Modal').on('hidden.bs.modal', function () {
        onHideDeleteSchProjectModal();
    });
    
    $('#addNewCalendar_Modal').on('hidden.bs.modal', function () {
        onHideNewCalendarModal();
    });
    $('#updateCalendar_Modal').on('hidden.bs.modal', function () {
        onHideUpdateSchCalendarModal();
    });
    $('#deleteCalendar_Modal').on('hidden.bs.modal', function () {
        onHideDeleteSchCalendarModal();
    });

});






function loadLabelsAjaxCallback2(data_labels) {
    var option = '';
    for (var i = 0; i < data_labels.length; i++) {
        option += '<option value="' + data_labels[i].id + '">' + data_labels[i].text + '</option>';
    }
    $('#updateSchLabel').append(option);
    $('#updateSchLabel').on('change', function () {
        var sVal = $(this).val();
        if (sVal === 'null') {
            onHideUpdateSchLabelModal();
            return;
        }
        getLabelById(sVal, getLabelByIdAjaxCallback);
    });

    $('#deleteSchLabel').append(option);
    $('#deleteSchLabel').on('change', function () {
        var sVal = $(this).val();
        if (sVal === 'null') {
            onHideDeleteSchLabelModal();
            return;
        }
        $('#deleteSchLabel_Modal form fieldset').removeAttr('disabled');
        $('#deleteLabel_Modal_id').val(sVal);
    });
}



function getLabelByIdAjaxCallback(label) {
    $('#updateSchLabel_Modal form fieldset').removeAttr('disabled');
    $('#updateLabel_Modal_id').val(label.id);
    $('#updateSchLabel_pname').val(label.text);
}


function addNewSchLabel() {
    
    var label = {
        text: $('#addNewSchLabel_pname').val()
    };
    addNewLabel(label, function () {
        $('#addNewSchLabel_Modal').modal('hide');
        location.reload();
    });
    
}

function updateSchLabel() {

    var label_id = $('#updateLabel_Modal_id').val();
    var label_name_txt = $('#updateSchLabel_pname').val();

    var label = {
        id: label_id,
        text: label_name_txt
    };
    updateLabel(label, function () {
        $('#updateSchLabel_Modal').modal('hide');
        location.reload();
    });    
}

function deleteSchLabel() {
    var project_name_txt = $('#deleteSchLabel_pname').val();
    if (project_name_txt !== 'DELETE') {
        alert('type DELETE');
    } else {
        deleteLabel($('#deleteLabel_Modal_id').val(), function() {
            $('#deleteSchLabel_Modal').modal('hide');
            location.reload();
        });        
    }
}

function onHideNewSchLabelModel() {
    $('#addNewSchLabel_pname').val('');
}

function onHideUpdateSchLabelModal() {
    $('#updateSchLabel').val('null');
    $('#updateSchLabel_pname').val('');
    $('#updateSchLabel_Modal form fieldset').attr('disabled', 'disabled');
}

function onHideDeleteSchLabelModal() {
    $('#deleteSchLabel').val('null');
    $('#deleteSchLabel_pname').val('');
    $('#deleteSchLabel_Modal form fieldset').attr('disabled', 'disabled');
}










function loadProjectsAjaxCallback2(data_projects) {
    var option = '';
    for (var i = 0; i < data_projects.length; i++) {
        option += '<option value="' + data_projects[i].id + '">' + data_projects[i].text + '</option>';
    }
    $('#addNewSchProject_pname_parent').append(option);

    $('#updateSchProject_pname_parent').append(option);
    $('#updateSchProject_p').append(option);
    $('#updateSchProject_p').on('change', function () {
        var sVal = $(this).val();
        if (sVal === 'null') {
            onHideUpdateSchProjectModal();
            return;
        }
        getProjectByid(sVal, getProjectByidAjaxCallback);
    });

    $('#deleteSchProject_p').append(option);
    $('#deleteSchProject_p').on('change', function () {
        var sVal = $(this).val();
        if (sVal === 'null') {
            onHideDeleteSchProjectModal();
            return;
        }
        $('#deleteProject_Modal form fieldset').removeAttr('disabled');
        $('#deleteProject_Modal_id').val(sVal);
    });
    
    
    $('#defaultSchProject_p').append(option);
    $('#defaultSchProject_p').on('change', function () {
        var sVal = $(this).val();
        if (sVal === 'null') {
            return;
        }
        setDefaultProject(sVal, function () {});
    });
    getDefaultProject(function (result) {
        if (result.project_id !== null) {
            $('#defaultSchProject_p').val(result.project_id);
        }        
        else {
            $('#defaultSchProject_p').val('');
        }
    });
}


function getProjectByidAjaxCallback(p) {
    $('#updateProject_Modal form fieldset').removeAttr('disabled');
    $('#updateProject_Modal_id').val(p.id);
    $('#updateSchProject_pname_new').val(p.text);
    $('#updateSchProject_pname_parent').val(p.parent);
}


function addNewSchProject() {

    var project_name_txt = $('#addNewSchProject_pname').val();
    var parent_project_id = $("#addNewSchProject_pname_parent option:selected").val();

    var p = {
        text: project_name_txt,
        parent: parent_project_id
    };
    addNewProject(p, function () {        
        $('#addNewProject_Modal').modal('hide');
        location.reload();
    });
}

function updateSchProject() {
    var project_id = $('#updateProject_Modal_id').val();
    var project_name_txt = $('#updateSchProject_pname_new').val();
    var project_parent_id = $("#updateSchProject_pname_parent option:selected").val();

    var p = {
        id: project_id,
        text: project_name_txt,
        parent: project_parent_id
    };
    updateProject(p, function () {
        $('#updateProject_Modal').modal('hide');
        location.reload();
    });    
}

function deleteSchProject() {
    var project_name_txt = $('#deleteSchProject_pname_new').val();
    if (project_name_txt !== 'DELETE') {
        alert('type DELETE');
    } else {
        deleteProject($('#deleteProject_Modal_id').val(), function () {
            $('#deleteProject_Modal').modal('hide');
            location.reload();
        });        
    }
}

function onHideNewProjectModal() {
    $('#addNewSchProject_pname').val('');
    $('#addNewSchProject_pname_parent').val('#');
}

function onHideUpdateSchProjectModal() {
    $('#updateSchProject_p').val('null');
    $('#updateSchProject_pname_new').val('');
    $('#updateSchProject_pname_parent').val('#');
    $('#updateProject_Modal form fieldset').attr('disabled', 'disabled');
}

function onHideDeleteSchProjectModal() {
    $('#deleteSchProject_p').val('null');
    $('#deleteSchProject_pname_new').val('');
    $('#deleteProject_Modal form fieldset').attr('disabled', 'disabled');
}










function loadCalendarsAjaxCallback2(data_calendars) {
    var option = '';
    for (var i = 0; i < data_calendars.length; i++) {
        option += '<option value="' + data_calendars[i].id + '">' + data_calendars[i].title + '</option>';
    }

    $('#updateSchCalendar_p').append(option);
    $('#updateSchCalendar_p').on('change', function () {
        var sVal = $(this).val();
        if (sVal === 'null') {
            onHideUpdateSchCalendarModal();
            return;
        }
        getCalendarByid(sVal, getCalendarByidAjaxCallback);
    });

    $('#deleteSchCalendar_p').append(option);
    $('#deleteSchCalendar_p').on('change', function () {
        var sVal = $(this).val();
        if (sVal === 'null') {
            onHideDeleteSchCalendarModal();
            return;
        }
        $('#deleteCalendar_Modal form fieldset').removeAttr('disabled');
        $('#deleteCalendar_Modal_id').val(sVal);
    });
    
    
    $('#defaultSchCalendar_p').append(option);
    $('#defaultSchCalendar_p').on('change', function () {
        var sVal = $(this).val();
        if (sVal === 'null') {
            return;
        }
        setDefaultCalendar(sVal, function () {});
    });
    getDefaultCalendar(function (result) {
        if (result.calendar_id !== null) {
            $('#defaultSchCalendar_p').val(result.calendar_id);
        } 
        else {
            $('#defaultSchCalendar_p').val('');
        }
    });
    
}



function getCalendarByidAjaxCallback(p) {
    $('#updateCalendar_Modal form fieldset').removeAttr('disabled');
    $("#updateSchCalendar_pcolor").spectrum("enable");
    $('#updateCalendar_Modal_id').val(p.id);
    $('#updateSchCalendar_pname_new').val(p.title);
    
    var pcolor = null;
    if (p.bckgColor !== null) {
        pcolor = p.bckgColor;
}
    else {
        pcolor = DEFAULT_COLOR;
    }
    $("#updateSchCalendar_pcolor").spectrum('set', pcolor);    
}



function addNewSchCalendar() {

    var calendar_name_txt = $('#addNewSchCalendar_pname').val();
    var calendar_bckgColor = $("#addNewSchCalendar_pcolor").spectrum('get').toHexString();

    var p = {
        title: calendar_name_txt,
        bckgColor: calendar_bckgColor
    };
    addNewCalendar(p, function () {        
        $('#addNewCalendar_Modal').modal('hide');
        location.reload();
    });
}

function updateSchCalendar() {
    var calendar_id = $('#updateCalendar_Modal_id').val();
    var calendar_name_txt = $('#updateSchCalendar_pname_new').val();
    var calendar_bckgColor = $("#updateSchCalendar_pcolor").spectrum('get').toHexString();

    var p = {
        id: calendar_id,
        title: calendar_name_txt,
        bckgColor: calendar_bckgColor
    };
    updateCalendar(p, function () {
        $('#updateCalendar_Modal').modal('hide');
        location.reload();
    });    
}

function deleteSchCalendar() {
    var calendar_name_txt = $('#deleteSchCalendar_pname_new').val();
    if (calendar_name_txt !== 'DELETE') {
        alert('type DELETE');
    } else {
        deleteCalendar($('#deleteCalendar_Modal_id').val(), function () {
            $('#deleteCalendar_Modal').modal('hide');
            location.reload();
        });        
    }
}

function onHideUpdateSchCalendarModal() {
    $('#updateSchCalendar_p').val('null');
    $('#updateSchCalendar_pname_new').val('');
    $("#updateSchCalendar_pcolor").spectrum({color: DEFAULT_COLOR});
    $('#updateCalendar_Modal form fieldset').attr('disabled', 'disabled');
    $("#updateSchLabel_pcolor").spectrum("disable");
}

function onHideDeleteSchCalendarModal() {
    $('#deleteSchCalendar_p').val('null');
    $('#deleteSchCalendar_pname_new').val('');
    $('#deleteCalendar_Modal form fieldset').attr('disabled', 'disabled');
}

function onHideNewCalendarModal() {
    $('#addNewSchCalendar_pname').val('');
    $("#addNewSchCalendar_pcolor").spectrum({color: DEFAULT_COLOR});
}