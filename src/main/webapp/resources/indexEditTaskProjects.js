function loadEditTaskProjects(data_projects) {
    
    $('.edit-task-projects').jstree({
        "core": {
            'themes': {
                'name': 'proton',
                'responsive': true
            },
            "check_callback": true,
            'data': data_projects,
            'multiple': false
        },
        "checkbox": {
            "three_state": false,
            "cascade": 'undetermined'
        },
        "plugins": ["checkbox"]
    });



    $('.edit-task-projects').on('loaded.jstree', function () {
        ($(this)).jstree('open_all');
    });
    $('#selectedProject_Modal_ok').on('click', function () {
        var jsTreeInst = $('.edit-task-projects').jstree(true);
        var selectedProjectJSTreeInst = jsTreeInst.get_selected();
        var selectedProjectId = $('.edit-task-projects-id').val();
        var selectedProjectText = "";

        if (selectedProjectJSTreeInst.length > 0) {
            selectedProjectId = selectedProjectJSTreeInst[0];
            var node = jsTreeInst.get_node(selectedProjectJSTreeInst);
            selectedProjectText = node.text;

            if (selectedProjectId !== $('.edit-task-projects-id').val()) {
                var task_id = $('.task_ProjectModal_Task_id').val();
                var parent = "#";
                var parent_text = "";
                changeEditTaskParent(selectedProjectId, parent, task_id, parent_text);
            }


            $('.edit-task-projects-id').val(selectedProjectId);
            $('.selectedProject_span').text(selectedProjectText);
        }


    });
    $('#selectedProject_Modal').on('hidden.bs.modal', function () {
        $('.edit-task-projects').jstree("deselect_all");
        $('.edit-task-projects').jstree('select_node', $('.edit-task-projects-id').val());
    });
    
}


function setEditTaskProject(project_id) {
    $('.edit-task-projects').jstree('deselect_all');
    $('.edit-task-projects').jstree('select_node', project_id);
    $('.edit-task-projects-id').val(project_id);
}

function getEditTaskProject() {
    return $('.edit-task-projects-id').val();
}