var selectizeDesktop;
var selectizeModal;

function loadLabelsAjaxCallback(data_labels) {
    fillDropdown($('#sch_labels'), data_labels, function (data) {
        showTasksWrapper();
        hideCalendarWrapper();
        deselectProjectMenu();

        $('.selected_view').text($(this).find('strong').text());

        $('input[name="selectedView"]').val('labels');
        $('input[name="selectedValue"]').val($(this).attr('data-id'));
        setGroupAndSort();
        refreshData();
    });

    selectizeDesktop = selectizeLabels('#formBodyDesktop form fieldset .labels .labels-sch', data_labels);
    selectizeMobile = selectizeLabels('#formBodyModal form fieldset .labels .labels-sch', data_labels);
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