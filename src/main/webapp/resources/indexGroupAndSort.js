function loadGroupAndSort() {
    
    $('.page-header .dropdown-menu li').on('click', function () {                
        $(this).parent('ul').children().find('.gsli').removeClass('glyphicon-ok-circle');
        $(this).find('.gsli').addClass('glyphicon-ok-circle');
        
        var group = $('.ulGroup').find('.glyphicon-ok-circle').attr('id');
        var sort = $('.ulSort').find('.glyphicon-ok-circle').attr('id');
        if (group.startsWith('group-sdate') && sort.startsWith('sort-sdate')) {
            sort = 'dont-sort';
            $('.ulSort').children().find('.gsli').removeClass('glyphicon-ok-circle');
            $('#' + sort).addClass('glyphicon-ok-circle');
        }
        disableItem();
        saveGroupAndSort(group, sort);
    });
    
}

function disableItem() {    
    var group = $('.ulGroup').find('.glyphicon-ok-circle').attr('id');
    if (group.startsWith("group-sdate")) {
        $('#sort-sdate-up').parents('li').hide();
        $('#sort-sdate-down').parents('li').hide();
    }
    else {            
        $('#sort-sdate-up').parents('li').show();
        $('#sort-sdate-down').parents('li').show();
    }
    
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
        disableItem();
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

function getSortingText() {
    var res = '';
    var selected = $('.ulSort').find('.glyphicon-ok-circle');
    var sort = selected.attr('id');
    if (sort !== 'dont-sort') {
        $('#qwe_temp').html('');
        res = $('#qwe_temp').append(selected.parent().find('span:first-child').clone()).html() + selected.parent().text();
    }
    return res;
}

function getGroupingText() {
    var res = '';
    var selected = $('.ulGroup').find('.glyphicon-ok-circle');
    var group = selected.attr('id');
    if (group !== 'dont-group') {
        $('#qwe_temp').html('');
        if (group.startsWith('group-sdate')) {
            res = $('#qwe_temp').append(selected.parent().find('span:first-child').clone()).html() + selected.parent().text();
        }
        else {
            res = selected.parent().text();
        }
        
    }
    return res;
}