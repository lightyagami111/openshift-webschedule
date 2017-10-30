var selectizeDesktop;
var selectizeModal;

function loadLabelsAjaxCallback(data_labels) {
    fillDropdownLabel($('#sch_labels'), data_labels, function (data) {
        showTasksWrapper();
        hideCalendarWrapper();
        deselectProjectMenu();

        selectedView = 'labels';
        selectedValue = $(this).attr('data-id');
        setGroupAndSort();
        refreshData();
    });

    selectizeDesktop = selectizeLabels('#formBodyDesktop form fieldset .labels .labels-sch', data_labels);
    selectizeMobile = selectizeLabels('#formBodyModal form fieldset .labels .labels-sch', data_labels);
}


function fillDropdownLabel(ulElemnt, data, functionOnClick) {
    var dropdownElHtmlDivider = $('#label_item_menu_template_1').html();
    var dropdownElHtml = $('#label_item_menu_template_0').html();
    for (var j = 0; j < data.length; j++) {
        var newSdropdownElHtml = (j > 0) ? $(dropdownElHtmlDivider) : $(dropdownElHtml);
        newSdropdownElHtml.attr('data-id', data[j].id);
        newSdropdownElHtml.on('click', functionOnClick);
        newSdropdownElHtml.find('strong').prepend(data[j].text);
        $(newSdropdownElHtml).find('strong').css('font-size', '17px');

        ulElemnt.append(newSdropdownElHtml);
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