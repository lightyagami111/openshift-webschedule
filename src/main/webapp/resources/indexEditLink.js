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
        saveLink({url: $('#link_input').val(), taskId: links_taskId}, function (result) {
            links_items.push({id: result.id, title: result.title, url: result.url});
            $('#link_input').val('');
            loadList(links_modal, links_items);
            // set button to 
            $(links_modal + ' button').prop('disabled', true);
        });
    });



    // delete one item
    $(links_modal + ' ul').delegate("span", "click", function (event) {
        event.stopPropagation();
        var index = $(this).parents('li').attr('data-id');
        deleteLink(index, links_taskId, function () {
            $(links_modal + ' li[data-id="' + index + '"]').remove();
            removeLink(links_items, index);
        });
    });



    // edit panel
    $(links_modal + ' ul').delegate("a", 'click', function () {
        var index = $(this).parents('li').attr('data-id');
        var item = findLink(index, links_items);
        $('#edit_link_title').val(item.title);
        $('#edit_link_url').val(item.url);
        $('#edit_link_id').val(item.id);
    });

    $('#edit_link_button').click(function () {
        editItem(links_items, $('#edit_link_id').val(), $('#edit_link_title').val(), $('#edit_link_url').val());
        loadList(links_modal, links_items);

        updateLink({
            id: $('#edit_link_id').val(),
            title: $('#edit_link_title').val(),
            url: $('#edit_link_url').val()
        }, function () {});

    });


});

function loadLinksList(items1, taskId1) {
    links_taskId = taskId1;
    links_items = items1;
    loadList(links_modal, items1);
}


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

function loadList(links_modal, items) {
    items = items.sort(function (a, b) {
        return ((a.id < b.id) ? -1 : ((a.id > b.id) ? 1 : 0));
    });
    $(links_modal + ' li').remove();
    if (items.length > 0) {
        for (var i = 0; i < items.length; i++) {
            var li = $(link_item_html);
            li.attr('data-id', items[i].id);

            var aLink = li.find('a[target="_blank"]');
            aLink.attr('href', items[i].url);
            aLink.text(items[i].title);

            $(links_modal + ' ul').append(li);
        }
    }
}


function findLink(id, items) {
    for (var i = 0; i < items.length; i++) {
        if (items[i].id == id) {
            return items[i];
        }
    }
}

function editItem(items, id, title, url) {
    for (var i = 0; i < items.length; i++) {
        if (items[i].id == id) {
            items[i].title = title;
            items[i].url = url;
            break;
        }
    }
}

function removeLink(items, index) {
    var indexToRemove = null;
    for (var i = 0; i < items.length; i++) {
        if (items[i].id == index) {
            indexToRemove = i;
            break;
        }
    }
    if (indexToRemove !== null) {
        items.splice(indexToRemove, 1);
    }
}