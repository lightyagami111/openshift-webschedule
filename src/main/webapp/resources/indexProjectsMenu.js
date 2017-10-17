function loadProjectsMenu(data_projects) {

    $('#side-menu').jstree({
        "core": {
            'themes': {
                'name': 'proton',
                'responsive': true
            },
            "check_callback": true,
            'data': data_projects
        }
    });

    $('#side-menu').on("select_node.jstree", function (e, data) {
        var project_id = data.node.id;
        var project_title = data.node.text;
        showTasksWrapper();
        hideCalendarWrapper();

        $('.selected_view').text(project_title);
        $('input[name="selectedView"]').val('projects');
        $('input[name="selectedValue"]').val(project_id);
        setGroupAndSort();
        refreshData();
    });

    $('#side-menu').on('loaded.jstree', function () {
        ($(this)).jstree('open_all');
        colorizefIterate(data_projects);
    });

    $('#side-menu').on('open_node.jstree', function () {
        colorizefIterate(data_projects);
    });





    if (mobileCheck() === false) {
        $('.sidebar').css({
            'width': '400px'
        });
        $('#mobileMenuButton').hide();
    }

}



function colorizefIterate(data_projects) {
    $('#side-menu li').each(function (index, element) {
        colorizeF(data_projects, element);
    });
}


function colorizeF(data_projects, element) {
    var id = $(element).attr('id');
    var a = $(element).find('a[id="'+id+'_anchor"]');
    if (a.hasClass('color-box-text') === false) {
        var project = getProjectByid_loaded(data_projects, id);
        var bckgColor = project.bckgColor;
        if (bckgColor !== null && bckgColor.toUpperCase() !== DEFAULT_COLOR) {
            a.css('background-color', bckgColor);
        }
        a.addClass('color-box-text');
    }
}



function deselectProjectMenu() {
    $('#side-menu').jstree("deselect_all");
}