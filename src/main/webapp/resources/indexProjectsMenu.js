
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
        showTasksWrapper();
        hideCalendarWrapper();

        selectedView = 'projects';
        selectedValue = project_id;
        setGroupAndSort();
        refreshData();
    });

    $('#side-menu').on('loaded.jstree', function () {
        ($(this)).jstree('open_all');
        node_css_iterate(data_projects);
    });

    $('#side-menu').on('open_node.jstree', function () {
        node_css_iterate(data_projects);
    });





    if (mobileCheck() === false) {
        $('.sidebar').css({
            'width': '400px'
        });
        $('#mobileMenuButton').hide();
    }

}



function node_css_iterate(data_projects) {
    $('#side-menu li').each(function (index, element) {
        node_css(element);
    });
}


function node_css(element) {
    var id = $(element).attr('id');
    var a = $(element).find('a[id="'+id+'_anchor"]');
    if (a.hasClass('color-box-text') === false) {
        a.addClass('color-box-text');
    }
}



function deselectProjectMenu() {
    $('#side-menu').jstree("deselect_all");
}