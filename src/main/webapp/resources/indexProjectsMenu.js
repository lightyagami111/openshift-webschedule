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
        disableItemGroupDropdown();
        setGroupAndSort();
        refreshData();
    });
    
    $('#side-menu').on('loaded.jstree', function () {
        ($(this)).jstree('open_all');
        $('#side-menu li a').each(function (index, element) {
            colorizeF(data_projects, element);
        });
    });
    
    $('#side-menu').on('open_node.jstree', function () {
        $('#side-menu li a').each(function (index, element) {
            colorizeF(data_projects, element);
        });
    });
    
    
    
    
    
    if (mobileCheck() === false) {
        $('.sidebar').css({
            'width': '400px'
        });
    }

}





function colorizeF(data_projects, element) {
    if ($(element).find('.color-box').length === 0) {
        var project = getProjectByid_loaded(data_projects, $(element).attr('id').replace('_anchor', ''));
        var bckgColor = project.bckgColor;
        var e = $('<div class="color-box"></div>');
        if (bckgColor !== null && bckgColor.toUpperCase() !== DEFAULT_COLOR) {
            e.css('background-color', bckgColor);
        }
        $(element).after(e);
        $(element).addClass('color-box-text');
    }
}



function deselectProjectMenu() {
    $('#side-menu').jstree("deselect_all");
}