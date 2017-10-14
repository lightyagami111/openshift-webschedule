function loadGroupAndSort() {
    
    $('.page-header .dropdown-menu li').on('click', function () {                
        $(this).parent('ul').children().find('.gsli').removeClass('glyphicon-ok-circle');
        $(this).find('.gsli').addClass('glyphicon-ok-circle');
        
        var group = $('.ulGroup').find('.glyphicon-ok-circle').attr('id');
        var sort = $('.ulSort').find('.glyphicon-ok-circle').attr('id');
        disableItemSortDropdown();
        saveGroupAndSort(group, sort);
    });

    if (mobileCheck() === true) {
        $('.page-header').children().each(function () {
            $(this).css({
                'margin-left': '-8px'
            });
        });
        $('.gs').find('.dropdown-menu').addClass('dropdown-menu-right');
    }
    
}

function disableItemSortDropdown() {    
    var group = $('.ulGroup').find('.glyphicon-ok-circle').attr('id');
    if (group.startsWith("group-sdate")) {
        $('#sort-sdate-up').parents('li').hide();
        $('#sort-sdate-down').parents('li').hide();
    }
    else {            
        $('#sort-sdate-up').parents('li').show();
        $('#sort-sdate-down').parents('li').show();
    }
}


function disableItemGroupDropdown() {
    var view = $('input[name="selectedView"]').val();
    if (view === 'projects') {
        $('#group-project').parents('li').hide();
        $('#group-label').parents('li').show();
    } else if (view === 'labels') {
        $('#group-label').parents('li').hide();
        $('#group-project').parents('li').show();
    }
}
function setGroupAndSort() {
    var value = $('input[name="selectedValue"]').val();
    var view = $('input[name="selectedView"]').val();

    findGS(view, value, function (result) {
        $('.ulGroup').children().find('.gsli').removeClass('glyphicon-ok-circle');
        $('.ulSort').children().find('.gsli').removeClass('glyphicon-ok-circle');
        $('#' + result.sort_).addClass('glyphicon-ok-circle');
        $('#' + result.group_).addClass('glyphicon-ok-circle');
        disableItemSortDropdown();
    });
}
function saveGroupAndSort(group_, sort_) {
    var value_ = $('input[name="selectedValue"]').val();
    var view_ = $('input[name="selectedView"]').val();
    saveOrUpdateGS({
        view: view_,
        id: value_,
        group: group_,
        sort: sort_
    }, function () {
        refreshData();
    });
}