function loadTaskParent() {
    $('.edit-task-parent').jstree({
        "core": {
            'themes': {
                'name': 'proton',
                'responsive': true
            },
            "check_callback": true,
            'multiple': false
        },
        "checkbox": {
            "three_state": false,
            "cascade": 'undetermined'
        },
        "plugins": ["checkbox"]
    });
    $('#selectedParent_Modal_ok').on('click', function () {
        var jsTreeInst = $('.edit-task-parent').jstree(true);
        var selectedParentJSTreeInst = jsTreeInst.get_selected();
        var selectedParentId = "#";
        var selectedParentText = "";

        if (selectedParentJSTreeInst.length > 0) {
            selectedParentId = selectedParentJSTreeInst[0];
            var node = jsTreeInst.get_node(selectedParentJSTreeInst);
            selectedParentText = node.text;
        }

        $('.edit-task-parent-id').val(selectedParentId);
        $('.selectedParent_span').text(selectedParentText);
    });
    $('#selectedParent_Modal').on('hidden.bs.modal', function () {
        $('.edit-task-parent').jstree("deselect_all");
        $('.edit-task-parent').jstree('select_node', $('.edit-task-parent-id').val());
    });
}


function changeEditTaskParent(project_id, parent, id, parent_text) {
    var currentProjectTasks = [];
    var jsTreeInst = $('.edit-task-parent').jstree(true);
    loadTasksByProject(project_id, function (result) {
        currentProjectTasks = result;
        jsTreeInst.settings.core.data = currentProjectTasks;
        jsTreeInst.refresh();
    });
    $('.edit-task-parent').on('refresh.jstree', function (e, data) {
        $('.edit-task-parent').jstree("deselect_all");
        $('.edit-task-parent').jstree("open_all");

        var els = currentProjectTasks;
        for (var i = 0; i < els.length; i++) {
            var node = jsTreeInst.get_node(els[i].id);
            jsTreeInst.enable_node(node);
        }

        jsTreeDisableCheckboxes(jsTreeInst, id);
        $('.edit-task-parent').jstree('select_node', parent);
        $('.edit-task-parent-id').val(parent);
        $('.selectedParent_span').text(parent_text);
    });
}


function getTaskParent() {
    return $('.edit-task-parent-id').val();
}

function getTaskParentText(form) {
    return form.find('.selectedParent_span').text();
}