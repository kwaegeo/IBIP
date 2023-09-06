$(document).ready(function () {
    $('body').append('<div class="cm-loading" style="display:none"><div class="lds-spinner"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div></div>');
    $.ajaxSetup({
        beforeSend: function () {
            //로딩 추가
            $(".cm-loading").show();
        },
        complete: function () {
            $(".cm-loading").hide();
        },
        contentType: 'application/json; charset=utf-8'
    });


})

