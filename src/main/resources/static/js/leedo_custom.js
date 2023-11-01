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


$(document).ajaxComplete(function (event, xhr, settings){
    if(xhr.status === 200){
        var responseData = JSON.parse(xhr.responseText);
        if(responseData.code === "M04"){
            alert("세션이 만료되었습니다. 다시 로그인해주세요.");
            window.location.href = "/login";
        }
    }
})