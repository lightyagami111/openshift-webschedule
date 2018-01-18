var selectizeDesktop;
var selectizeModal;

var selectedLabelsId = [];

function loadLabelsAjaxCallback(data_labels) {
    fillDropdownLabel(data_labels);

    selectizeDesktop = selectizeLabels('#formBodyDesktop form fieldset .labels .labels-sch', data_labels);
    selectizeMobile = selectizeLabels('#formBodyModal form fieldset .labels .labels-sch', data_labels);
    
    $('#showLabels').on('click', function() {
        showLabels();
    });
}


function fillDropdownLabel(data) {
    var dropdownElHtmlDivider = $('#item_menu_template_1').html();
    var dropdownElHtml = $('#item_menu_template_0').html();


    for (var j = 0; j < data.length; j++) {
        var newSdropdownElHtml = (j > 0) ? $(dropdownElHtmlDivider) : $(dropdownElHtml);
        newSdropdownElHtml.attr('data-id', data[j].id);
        newSdropdownElHtml.find('strong').prepend(data[j].text);
        $(newSdropdownElHtml).find('strong').css('font-size', '17px');

        newSdropdownElHtml.find('a').on('click', function (event) {
            var $target = $(event.currentTarget);
            var val = $(this).parent('li').attr('data-id');
            var $inp = $target.find('input');
            
            if ((selectedLabelsId.indexOf(val)) > -1) {
                if (selectedLabelsId.length===1) {
                    return false;
                }
                removeA(selectedLabelsId,val);
                $inp.prop('checked', false);
            } else {
                selectedLabelsId.push(val);
                $inp.prop('checked', true);
            }
            
            setSelectedLabel(selectedLabelsId, function() {
                refreshData();
            }); 
            
            $(event.target).blur();

            return false;
        });        

        $('#sch_labels').append(newSdropdownElHtml);
    }
}

function selectizeLabels(inputId, data) {
    $(inputId).val('');
    var $select = $(inputId).selectize({
        plugins: ['remove_button'],
        delimiter: ',',
        valueField: 'id',
        labelField: 'text',
        maxItems: null
    });
    var selectize = $select[0].selectize;
    selectize.addOption(data);
    $(inputId + '-selectized').attr('readonly', true);
    return selectize;
}

function getSelectizeLabels(form) {
    var sel = null;
    if (form.attr('id') === 'formBodyDesktop') {
        sel = selectizeDesktop;
    } else if (form.attr('id') === 'formBodyModal') {
        sel = selectizeMobile;
    }
    return sel;
}


function showLabels() {
    showTasksWrapper();
    hideCalendarWrapper();
    deselectProjectMenu();

    selectedView = 'labels';
    setGroupAndSort();
    refreshData();
}