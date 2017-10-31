/* global moment, selectedView, selectedValue */

var task_list_template = $('#tasks_list_template').html();

function refreshTasksListsCallback(result) {
    $('#tasks_list_content').html('');
    var groups = result.groups;
    var view = result.view;
    var allowNewTaskAction = result.allowNewTaskAction;
    for (var i = 0; i < groups.length; i++) {
        var d1 = groups[i];
        var thtml = $(task_list_template);
        thtml.find('.selected_grouping').text(d1.groupBy);
        loadJsTree(thtml, d1.tasks, allowNewTaskAction);
        $('#tasks_list_content').append(thtml);
    }
    $('#tasks_list_content').find('.selected_view').text(view);

    $('#tasks_list_content').find('.selected_sorting').html(getSortingText());
    $('#tasks_list_content').find('.selected_grouping_id').html(getGroupingText());

}

function loadJsTree(thtml, data_tasks, allowNewTaskAction) {
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
        showMoreInfoIterate(data_tasks, jstreeImpl);
    });

    jstreeImpl.on('loaded.jstree', function () {
        ($(this)).jstree('open_all');
        showMoreInfoIterate(data_tasks, jstreeImpl);
    });

    jstreeImpl.on('open_node.jstree', function () {
        showMoreInfoIterate(data_tasks, jstreeImpl);
    });

    if (allowNewTaskAction === true) {
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
    } else {
        thtml.find('.newTaskButtonsPosition').hide();
    }
}

function showMoreInfoIterate(data_tasks, jstreeImpl) {
    jstreeImpl.find('li').each(function (index, element) {
        showMoreInfo(data_tasks, element);
    });
}

function showMoreInfo(data_tasks, element) {
    var id = $(element).attr('id');
    var a = $(element).find('a[id="' + id + '_anchor"]');
    if ($(a).parent().find('.moreInfo').length === 0) {
        var task = getTaskById_loaded(data_tasks, $(element).attr('id').replace('_anchor', ''));
        if (task !== null) {
            var m = $('<span class="moreInfo"></span>');
            var haveAppendedText = false;



            var mDate = $('<span class="moreInfoDate"></span>');
            showMoreInfoDate(task, mDate);
            if (mDate.text().length > 0) {
                haveAppendedText = true;
            } 
            if (task.rep !== null) {
                var repNextFire = task.repNextFire;
                if (repNextFire !== null) {
                    haveAppendedText = true;
                    mDate.addClass('glyphicon glyphicon-registration-mark');
                    showMoreInfoDate(repNextFire, mDate);
                }
            }
            m.append(mDate);




            if (task.priority !== 1) {
                var append = '';
                if (haveAppendedText === true) {
                    append = '&nbsp;&nbsp;|| ';
                }
                m.append(append + 'prio ' + task.priority);
                haveAppendedText = true;
            }



            if (task.labels !== null) {
                var labels_text = '';
                for (var i = 0; i < task.labels.length; i++) {
                    labels_text = labels_text + task.labels[i].text + ', ';
                }

                if (labels_text.length > 0) {
                    var append = '';
                    if (haveAppendedText === true) {
                        append = '&nbsp;&nbsp;|| ';
                    }
                    m.append(append + 'labels ' + labels_text);
                }
            }



            $(a).after(m);
        }
    }
}

function showMoreInfoDate(data, el) {
    var momentFormat = 'dd DD MMM YYYY';
    if (data.allDay === false) {
        momentFormat = 'dd DD MMM YYYY h:mm a';
    }
    if (data.start !== null) {
        var moment_start = moment(data.start).local();
        el.append(moment_start.format(momentFormat));
        if (moment_start.isBefore(moment().local(), 'day')) {
            el.css({
                'color': 'red'
            });
        }
        if (moment_start.isSame(moment().local(), 'day')) {
            el.css({
                'color': 'green'
            });
        }
    }
    if (data.end !== null) {
        var moment_start = moment(data.start).local();
        var moment_end = moment(data.end).local();
        el.append(' - ' + moment(data.end).local().format('h:mm a'));
        if ((moment_start.isBefore(moment().local(), 'day') || moment_start.isSame(moment().local(), 'day'))
                &&
                (moment_end.isAfter(moment().local(), 'day') || moment_end.isSame(moment().local(), 'day'))) {
            el.css({
                'color': 'green'
            });
        }
        if (moment_start.isBefore(moment().local(), 'day') && moment_end.isBefore(moment().local(), 'day')) {
            el.css({
                'color': 'red'
            });
        }
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

    if (selectedView === 'projects') {
        loadInitialTaskDataAjax(selectedValue, parent_id, title, null);
    } else { //selectedView is labels
        if (parent_id === '#') {
            getDefaultProject(function (result) {
                loadInitialTaskDataAjax(result.project_id, parent_id, title, [selectedValue]);
            });
        } else {
            getParentTaskProject(parent_id, function (result) {
                loadInitialTaskDataAjax(result.parent_project_id, parent_id, title, [selectedValue]);
            });
        }
    }

}

function loadInitialTaskDataAjax(project_id, parent_id, title, labels) {
    getDefaultCalendar(function(resultc) {
        loadInitialTaskData(resultc.calendar_id, project_id, parent_id, null, null, true, labels, function (result) {
            updateTaskTitle(result.id, title, function () {
                refreshData();
            });
        });
    });
}
