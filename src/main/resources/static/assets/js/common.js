// -------------------------------------- POPUP, 레이어팝업 --------------------------------------
$('.js-open').click(function (e) {
  e.preventDefault();

  var activeLayer = $(this).attr('data-pop');

  // 레이어 팝업 화면 가운데 정렬
  $('#' + activeLayer + '.popup-layer').css('position', 'absolute');
  $('#' + activeLayer + '.popup-layer').css('top', ($(window).height() - $('#' + activeLayer).outerHeight()) / 2 + $(window).scrollTop() + 'px');
  $('#' + activeLayer + '.popup-layer').css('left', ($(window).width() - $('#' + activeLayer).outerWidth()) / 2 + $(window).scrollLeft() + 'px');

  $('.popup-layer').addClass('hidden'); //모든 팝업 감추기(팝업안에 팝업이 또 있을때는 해당 안됨)
  $('#' + activeLayer).removeClass('hidden'); //호출한 팝업만 부르기
  $('.dimm').stop().show().css('z-index', '30'); //배경 가져오기
  $('body').css('overflow-y', 'hidden'); //body 스크롤 숨기기(화면고정)

  //닫기 버튼 , 배경 클릭 시
  $('#' + activeLayer)
    .children()
    .children('.js-close')
    .on('click', function () {
      $('#' + activeLayer).addClass('hidden'); //모든 팝업 감추기
      $('body').css('overflow-y', 'auto'); //body 스크롤 자동 원복
      $('.dimm').stop().hide().css('z-index', '11');
    });
});

// -------------------------------------- POPUP, 새창팝업 --------------------------------------
// win pop close
$('.pop-close').on('click', function () {
  window.close();
});

// -------------------------------------- navbar -----------------------------------------------
$('.navbar a').each(function () {
  $(this).on('click', function (e) {
    var parent = $(this).parent();
    var t = $(e.target);
    var tParent = t.parent().parent();

    if (parent.hasClass('active')) {
      parent.removeClass('active');
      t.next('ul').stop().slideUp();
    } else {
      if (tParent.hasClass('depth1')) {
        $('.depth1 li').removeClass('active');
        $('.depth2, .depth3').stop().slideUp();
        parent.addClass('active');
        t.next('ul').stop().slideDown();
      } else if (tParent.hasClass('depth2')) {
        $('.depth2 li').removeClass('active');
        $('.depth3').stop().slideUp();
        parent.addClass('active');
        t.next('ul').stop().slideDown();
      } else if (tParent.hasClass('depth3')) {
        $('.depth3 li').removeClass('active');
        parent.addClass('active');
        t.next('ul').stop().slideDown();
      }
    }
  });
});

// -------------------------------------- navbar -----------------------------------------------
$('.tbl-list.select-list tr').on('click', function () {
  $(this).toggleClass('choice');
});

// -------------------------------------- datepicker, 230209 --------------------------------------
$(document).ready(function () {
  $('.datepic input').each(function () {
    $(this).datepicker({
      dateFormat: 'yy-mm-dd',
      showOtherMonths: true,
      showMonthAfterYear: true,
      buttonText: '선택',
      yearSuffix: '년',
      monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
      monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
      dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
      dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
    });
  });
});

// ------------------------------------------- 연동처리버튼 토글, 230321 -------------------------------------------
$('.btn-toggle').on('click', function () {
  $(this).toggleClass('active');
});

$('.step-bottom .btn-toggle').on('click', function () {
  $(this).parent().parent('.box-step').toggleClass('complete');
});

// ------------------------------------------- tree, 230228 -------------------------------------------
$('.treeview').treeview({
  animated: 'fast',
  collapsed: true,
  unique: true,
  persist: 'cookie',
  toggle: function () {
    window.console && console.log('%o was toggled', this);
  },
});
