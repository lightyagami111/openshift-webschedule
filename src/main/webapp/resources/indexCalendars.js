/* global moment, selectedCalEvent, $c */
var selectedCalendarsId = [];

function loadCalendarsAjaxCallback(data_calendars) {
    var option = '';
    for (var i = 0; i < data_calendars.length; i++) {
        option += '<option value="' + data_calendars[i].id + '">' + data_calendars[i].title + '</option>';
    }
    $('.task_cal').append(option);

    fillDropdownCalendar($('#selectedCalendars'), data_calendars);
}


function fillDropdownCalendar(ulElemnt, data) {
    
    var dropdownElHtmlDivider = $('#calendar_item_menu_template_1').html();
    var dropdownElHtml = $('#calendar_item_menu_template_0').html();
    
    for (var j = 0; j < data.length; j++) {
        var newSdropdownElHtml = (j > 0) ? $(dropdownElHtmlDivider) : $(dropdownElHtml);
        newSdropdownElHtml.attr('data-id', data[j].id);
        newSdropdownElHtml.find('strong').prepend(data[j].title);
        newSdropdownElHtml.find('strong').css('font-size', '17px');
        
        newSdropdownElHtml.find('a').css('background-color', data[j].bckgColor);
        newSdropdownElHtml.find('a').css('color', $c.complement(data[j].bckgColor));        
        
        newSdropdownElHtml.find('a').on('click', function (event) {
            var $target = $(event.currentTarget);
            var val = $(this).parent('li').attr('data-id');
            var $inp = $target.find('input');
            
            if ((selectedCalendarsId.indexOf(val)) > -1) {
                if (selectedCalendarsId.length===1) {
                    return false;
                }
                removeA(selectedCalendarsId,val);
                $inp.prop('checked', false);
            } else {
                selectedCalendarsId.push(val);
                $inp.prop('checked', true);
            }

            setSelectedCalendar(selectedCalendarsId, function() {
                refreshData();
            });            
            
            $(event.target).blur();

            return false;
        });
        
        newSdropdownElHtml.find('input').prop('checked', false);
        if (data[j].selected===true) {
            selectedCalendarsId.push(data[j].id.toString());
            newSdropdownElHtml.find('input').prop('checked', true);
        }

        ulElemnt.append(newSdropdownElHtml);
    }
}



function fullCalendarOptions() {

    var h = {
        left: 'prev,next today',
        center: 'title',
        right: 'listWeek,month,basicWeek,basicDay'
    };
    if (mobileCheck() === true) {
        h = {
            left: 'prev,next today listWeek,month,basicWeek,basicDay title',
            center: '',
            right: 'title'
        };
    }
    $('#calendar').fullCalendar({
        header: h,
        views: {
            week: {
                titleFormat: 'MMMM DD YYYY',
                columnFormat: 'MMM DD ddd'
            },
            month: {
                titleFormat: 'MMMM YYYY'
            }
        },
        defaultView: 'listWeek',
        timeFormat: 'HH:mm',
        displayEventEnd: true,
        aspectRatio: mobileCheck() ? 1 : 1.35,
        firstDay: 1,
        selectable: true,
        selectHelper: true,
        select: function (start, end) {
            var startD = start.local().toDate();
            var endD = null;
            if (end.diff(start, 'days') > 1) {
                end.subtract(1, 'days');
                endD = end.local().toDate();
                endD = moment(endD).format('YYYY-MM-DD');
            }
            startD = moment(startD).format('YYYY-MM-DD');

            showTaskInfoModal(null, startD, endD, false);
        },
        eventClick: function (calEvent, jsEvent, view) {
            selectedCalEvent = calEvent;
            showTaskInfoModal(calEvent.id, null, null, true);
        },
        events: function (start, end, timezone, callback) {
            var startD = start.local().format('YYYY-MM-DD');
            var endD = end.local().format('YYYY-MM-DD');

            selectedView = 'calendar';
            selectedValue = startD + ' - ' + endD;

            loadEvents(startD, endD, function (result) {
                callback(result);
            });
        }
    });
    if (mobileCheck() === true) {
        $('.fc-left h2').before('<br>');
    }


    $('#showCalendar').on('click', function() {
        showCalendarWrapper();
        refreshData();
    });

}



function showCalendarWrapper() {
    $('#calendar-wrapper').show(250);
    $('#calendar').show(250);

    hideTasksWrapper();
    deselectProjectMenu();

    var view = $('#calendar').fullCalendar('getView');
    var startD = view.start.local().format('YYYY-MM-DD');
    var endD = view.end.local().format('YYYY-MM-DD');
    selectedView = 'calendar';
    selectedValue = startD + ' - ' + endD;
    $('#calendar').fullCalendar('option', 'contentHeight', "auto");

}