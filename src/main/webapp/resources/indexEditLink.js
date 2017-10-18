var links_modal = '#editLinks_Modal';
var link_modal = '#editLink_Modal';
var links_taskId = null;
var links_items = [];

$(document).ready(function () {

    // if input is empty disable button
    disableButton(links_modal);
    disableButton(link_modal);



    // bind input enter with button submit
    $('#link_input').keypress(function (e) {
        if (e.which === 13) {
            if ($('#link_input').val().length !== 0)
                $('#add_link_button').click();
        }
    });
    
    
    $('#add_link_button').click(function () {
        getTitleFromUrlLink({url: $('#link_input').val()}, function (result) {
            addToList({title: result.title, url: $('#link_input').val()});
            
            $('#link_input').val('');            
            // set button to 
            $(links_modal + ' button').prop('disabled', true);
        });
    });



    // delete one item
    $(links_modal + ' ul').delegate("span", "click", function (event) {
        event.stopPropagation();
        $(this).parents('li').remove();
    });



    // edit panel
    $(links_modal + ' ul').delegate("a", 'click', function () {
        var item = $(this).parents('li').find('a[target="_blank"]');
        $('#edit_link_title').val(item.text());
        $('#edit_link_url').val(item.attr('href'));
        
        $('#edit_link_button').unbind('click');
        $('#edit_link_button').click(function () {
            item.text($('#edit_link_title').val());
            item.attr('href', $('#edit_link_url').val());
        });
    });

});


function disableButton(formId) {
    $(formId + ' button').prop('disabled', true);
    $(formId + ' input').keyup(function () {
        if ($(this).val().length !== 0) {
            $(formId + ' button').prop('disabled', false);
        } else {
            $(formId + ' button').prop('disabled', true);
        }
    });
}



var link_item_html = $('#link_item_template').html();

function loadLinksList(items) {
    items = items.sort(function (a, b) {
        return ((a.id < b.id) ? -1 : ((a.id > b.id) ? 1 : 0));
    });
    $(links_modal + ' li').remove();
    if (items.length > 0) {
        for (var i = 0; i < items.length; i++) {
            var li = $(link_item_html);

            var aLink = li.find('a[target="_blank"]');
            aLink.attr('href', items[i].url);
            aLink.text(items[i].title);

            $(links_modal + ' ul').append(li);
        }
    }
}

function addToList(obj) {
    var li = $(link_item_html);

    var aLink = li.find('a[target="_blank"]');
    aLink.attr('href', obj.url);
    aLink.text(obj.title);
    
    $(links_modal + ' ul').append(li);
}


function getLinksList() {
    var result = [];
    $(links_modal + ' li').each(function () {
        var item = $(this).find('a[target="_blank"]');
        result.push({title: item.text(), url: item.attr('href')});
    });
    return result;
}