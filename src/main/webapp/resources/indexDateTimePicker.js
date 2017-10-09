function initDateTimePickers() {
    
    $('.datepicker-sch').val('');
    $('.timepicker-sch').val('');

    $('.datepicker-sch').pickadate({
        firstDay: 1
    });
    $('.timepicker-sch').pickatime({
        format: 'HH:i',
        interval: 10
    });
    if (mobileCheck() === true) {
        $('.datepicker-sch').css({
            'width': '44%'
        });
        $('.timepicker-sch').css({
            'width': '35%'
        });
    }
    
}

function setDatePickerPropDisabled(el_title, disable) {
    $('.datepicker-sch[title="'+el_title+'"]').prop('disabled', disable);
}



function setDateToPicker(title, jsDate) {
    var selector = '.datepicker-sch[title="'+title+'"]';
    if (jsDate === null) {
        $(selector).pickadate('picker').clear();
    } else {
        $(selector).pickadate('picker').set('select', jsDate);
    }
}

function setTimeToPicker(title, jsDate) {
    var selector = '.timepicker-sch[title="'+title+'"]';
    if (jsDate === null) {
        $(selector).pickatime('picker').clear();
    } else {
        jsDate = new Date(jsDate);
        $(selector).pickatime('picker').set('select', moment(jsDate).format("HH:mm"));
    }

}


function getDateFromPicker(title) {
    var el = $('.datepicker-sch[title="'+title+'"]');
    return el.pickadate('picker').get('select', 'yyyy-mm-dd');
}

function getTimeFromPicker(title) {
    var el = $('.timepicker-sch[title="'+title+'"]');
    return el.pickatime('picker').get('select', 'HH:i');
}



function getDateTimeData(datePickerTitle, timePickerTitle) {
    if (timePickerTitle === undefined) {
        timePickerTitle = datePickerTitle;
    }
    var startTime = getTimeFromPicker(timePickerTitle);
    if (startTime.length > 0) {
        startTime = 'T' + startTime + ':00.000Z';
    }
    var startDate = getDateFromPicker(datePickerTitle);
    if (startDate.length > 0) {
        startDate = startDate + startTime;
    }
    return startDate;
}

