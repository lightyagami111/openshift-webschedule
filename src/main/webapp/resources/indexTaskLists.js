var task_list_template = $('#tasks_list_template').html();

function refreshTasksListsCallback(result) {
    $('#tasks_list_content').html('');
    var groups = result.groups;
    var view = result.e.text;
    var bindNewTaskAction = result.bindNewTaskAction;
    for (var i = 0; i < groups.length; i++) {
        var d1 = groups[i];
        var thtml = $(task_list_template);
        thtml.find('.selected_grouping').text(d1.groupBy);
        loadJsTree(thtml, d1.tasks, bindNewTaskAction);
        $('#tasks_list_content').append(thtml);
    }
    $('#tasks_list_content').find('.selected_view').text(view);

}

function loadJsTree(thtml, data_tasks, bindNewTaskAction) {
    var jstreeImpl = thtml.find('div[action="taks_list"]');
    jstreeImpl.jstree({
        core: {
            themes: {
                'name': 'proton',
                'responsive': true
            },
            check_callback: true,
            data: data_tasks
        }
    });
    jstreeImpl.on('rename_node.jstree', function (e, data) {
        var title = data.text;
        if (title.length > 0) {
            var parent_id = jstreeImpl.jstree("get_parent", data.node.id);
            saveNewTask(parent_id, title);
        } else { //because refreshData(); is executed in saveNewTask
            refreshData();
        }
    });
    jstreeImpl.on("select_node.jstree", function (e, data) {
        var task_id = data.node.id;
        showTaskInfoMobileCheck(task_id);
    });
    jstreeImpl.jstree("deselect_all");
    jstreeImpl.jstree(true).refresh();
    jstreeImpl.on('refresh.jstree', function (e, data) {
        jstreeImpl.jstree(true).open_all();
    });

    if (bindNewTaskAction === true) {
        thtml.find('.newTaskButtonsPosition').show();
        thtml.find('a[action="newTaskToRoot"]').on('click', function () {
            newTaskToRoot(jstreeImpl);
        });
        thtml.find('a[action="newSubTask"]').on('click', function () {
            newSubTask(jstreeImpl);
        });
        thtml.find('a[action="newTaskToSameLevel"]').on('click', function () {
            newTaskToSameLevel(jstreeImpl);
        });
    }
    else {
        thtml.find('.newTaskButtonsPosition').hide();
    }
}




function newTaskToRoot(jstreeImpl, parent_id) {
    if (parent_id === undefined || parent_id === null) {
        parent_id = '#';
    }

    var ref = jstreeImpl.jstree('create_node', parent_id, {id: makeid(), text: ''}, 'last');
    jstreeImpl.jstree('edit', ref);
}

function newSubTask(jstreeImpl) {
    if (jstreeImpl.jstree("get_selected").length > 0) {
        newTaskToRoot(jstreeImpl, jstreeImpl.jstree("get_selected")[0]);
    } else {
        newTaskToRoot(jstreeImpl, null);
    }
}

function newTaskToSameLevel(jstreeImpl) {
    if (jstreeImpl.jstree("get_selected").length > 0) {
        newTaskToRoot(jstreeImpl, jstreeImpl.jstree("get_parent", jstreeImpl.jstree("get_selected")[0]));
    } else {
        newTaskToRoot(jstreeImpl, null);
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