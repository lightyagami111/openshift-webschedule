function loadTasksLists() {

    $('#taks_list').jstree({
        "core": {
            'themes': {
                'name': 'proton',
                'responsive': true
            },
            "check_callback": true
        }
    });
    $('#taks_list').on('rename_node.jstree', function (e, data) {
        var title = data.text;
        if (title.length > 0) {
            var parent_id = $("#taks_list").jstree("get_parent", data.node.id);
            saveNewTask(parent_id, title);
        } else { //because refreshData(); is executed in saveNewTask
            refreshData();
        }
    });
    $('#taks_list').on("select_node.jstree", function (e, data) {
        var task_id = data.node.id;
        showTaskInfoMobileCheck(task_id);
    });

}


function refreshTasksListsCallback(result) {
    $('#taks_list').jstree(true).settings.core.data = result;
    $('#taks_list').jstree("deselect_all");
    $('#taks_list').jstree(true).refresh();
    $('#taks_list').on('refresh.jstree', function (e, data) {
        $('#taks_list').jstree(true).open_all();
    });
}






function newTaskToRoot(parent_id) {
    if (parent_id === undefined || parent_id === null) {
        parent_id = '#';
    }

    var ref = $("#taks_list").jstree('create_node', parent_id, {id: makeid(), text: ''}, 'last');
    $('#taks_list').jstree('edit', ref);
}

function newSubTask() {
    if ($("#taks_list").jstree("get_selected").length > 0) {
        newTaskToRoot($("#taks_list").jstree("get_selected")[0]);
    } else {
        newTaskToRoot(null);
    }
}

function newTaskToSameLevel() {
    if ($("#taks_list").jstree("get_selected").length > 0) {
        newTaskToRoot($("#taks_list").jstree("get_parent", $("#taks_list").jstree("get_selected")[0]));
    } else {
        newTaskToRoot(null);
    }
}


function saveNewTask(parent_id, title) {
    if (parent_id === undefined || parent_id === null) {
        parent_id = '#';
    }

    if ($('input[name="selectedView"]').val() === 'projects') {
        loadInitialTaskDataAjax($('input[name="selectedValue"]').val(), parent_id, title, null);
    } else { //selectedView is labels
        if (parent_id === '#') {
            getDefaultProject(function (result) {
                loadInitialTaskDataAjax(result.project_id, parent_id, title, [$('input[name="selectedValue"]').val()]);
            });
        } else {
            getParentTaskProject(parent_id, function (result) {
                loadInitialTaskDataAjax(result.parent_project_id, parent_id, title, [$('input[name="selectedValue"]').val()]);
            });
        }
    }

}

function loadInitialTaskDataAjax(project_id, parent_id, title, labels) {
    loadInitialTaskData(project_id, parent_id, null, null, true, labels, function (result) {
        updateTaskTitle(result.id, title, function () {
            refreshData();
        });
    });
}