init = function() {
    var table = $('table#users').DataTable({
        'ajax' : '/data/users',
        'serverSide' : true,
        'lengthMenu' : [[10, 25, 50, -1], [10, 25, 50, 'All']],
        columns : [ {
            data : 'id'
        }, {
            data : 'name'
        }, {
            data : 'role'
        }, {
            data : 'status'
        }, {
            data : 'address.town',
            render: function(data, type, row) {
                return data ? data : '';
            }
        }, {
            data : 'likes'
        }]
    });

    function handleClick(columnId, selector) {
        return function() {
            $(this).toggleClass('active');
            var filter = '';
            $(selector).each(function() {
                filter += (filter === '' ? '' : '+') + $(this).text();
            });
            table.columns(columnId).search(filter).draw();
        };
    }
    $('div#role_selector button').click(
        handleClick(2, 'div#role_selector button.active'));
    $('div#status_selector button').click(
        handleClick(3, 'div#status_selector button.active'));

    function handleInputUpdate() {
        var likes_lowerbound = $('input#likes_lt').val(),
            likes_upperbound = $('input#likes_gt').val(),
            filter = likes_lowerbound + ':' + likes_upperbound;
        table.columns(5).search(filter).draw();
    }

    $('input#likes_lt').on('change paste keyup', handleInputUpdate);
    $('input#likes_gt').on('change paste keyup', handleInputUpdate);
};

function flatten(params) {
    params.columns.forEach(function (column, index) {
        params['columns[' + index + '].data'] = column.data;
        params['columns[' + index + '].name'] = column.name;
        params['columns[' + index + '].searchable'] = column.searchable;
        params['columns[' + index + '].orderable'] = column.orderable;
        params['columns[' + index + '].search.regex'] = column.search.regex;
        params['columns[' + index + '].search.value'] = column.search.value;
    });
    delete params.columns;

    params.order.forEach(function (order, index) {
        params['order[' + index + '].column'] = order.column;
        params['order[' + index + '].dir'] = order.dir;
    });
    delete params.order;

    params['search.regex'] = params.search.regex;
    params['search.value'] = params.search.value;
    delete params.search;

    return params;
}

$('table#sample').DataTable({
    'ajax': {
        'url': '/data/users',
        'type': 'GET',
        'data': flatten
    }
})

$(document).ready(init);
