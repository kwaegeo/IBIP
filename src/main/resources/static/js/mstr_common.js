/**
 * @Name  : mstr_common.js
 * @Description : MicroStrategy Customization javascript
 * @Modification Information
 *
 * @  수정일        수정자             수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2015.10.13   최성진             최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2015.10.13
 * @version 1.0
 * @see
 */


/**
 * 프롬프트 답변 XML 형식 반환 (계층구조 제외)
 * [type : vl(value), el(element), ob(object)]
 */
$.fn.getAnswerXML = function() {
	var frm = $(this);
	var xmlString = '';

	var mstr_custom = $(frm).find('[name^=mstr_custom]');

	// prompt loop
	if ($(mstr_custom).size() > 0) {
		$.each(mstr_custom, function(i, data) {
			var name = $(data).attr('name');

			// value prompt
			if (name.indexOf('vl') > -1) {
				if (name.indexOf('ym') == -1) {
					var value = $(data).val();
					if (name == 'mstr_custom_vl_yy' && $(data).next('select[name=mstr_custom_vl_ym]').val() != undefined) {
						value += $(data).next('select[name=mstr_custom_vl_ym]').val();
					}
					else if (name == 'mstr_custom_vl_ck') {
						if($(data).is(':checked') == false) {
							value = "";
						}
					}
					else if (name == 'mstr_custom_vl_tx' || name == 'mstr_custom_vl_yd' ) {
						if (value == $(data).attr('title')) {
							value = "";
						}
					}
					value = value.replace(/-/gi, "");
					xmlString += "<pa pt='4' pin='0' did='"+ $(data).attr('pid') +"' tp='10'>"+value+"</pa>";
				}
			}
			// element prompt
			else if (name.indexOf('el') > -1) {
				var resultXml = "<pa pt='7' pin='0' did='"+$(data).attr('pid')+"' tp='10'><mi><es>";

				// check box
				if (name.indexOf('ck') > -1) {
					if ($(data).find('input[id^=chk_'+$(data).attr('pid')+']:checked').size() > 0) {
						resultXml += "<at did='"+$(data).attr('aid')+"' tp='12'/>";

						$(data).find('input[id^=chk_'+$(data).attr('pid')+']:checked').each(function(n) {
							resultXml += "<e ei='"+$(this).val()+"' emt='1' disp_n='"+$(this).attr('disp_n')+"'/>";
						});
					}
				}
				// radio
				else if (name.indexOf('rd') > -1) {
					resultXml += "<at did='"+$(data).attr('aid')+"' tp='12'/>";
					resultXml += "<e ei='"+$(data).find('input[name=rd_'+$(data).attr('pid')+']:checked').val()+"' emt='1'/>";
				}
				// search
				else if (name.indexOf('sh') > -1) {  // 사업명 검색
					// resultXml += $('table[name=dataTable]').getAnswerXMLForSearchElement($(data).attr('aid'));  // DIV일 경우
					if ($(data).val() != '') {
						resultXml += "<at did='"+$(data).attr('aid')+"' tp='12'/>";
						resultXml += "<e ei='"+$(data).attr('aid')+":"+$(data).val()+"' emt='1'/>";
					}
				}
				// select
				else if (name.indexOf('sl') > -1) {
					if ($(data).val() != '선택하지 않음') {
						resultXml += "<at did='"+$(data).attr('aid')+"' tp='12'/>";
						resultXml += "<e ei='"+$(this).val()+"' emt='1' disp_n='"+$(this).attr('disp_n')+"'/>";
					}
				}
				// combo box(select box)
				else {
					if ($(data).val() != "") { // 전체 선택이 아니면
						var prefix = "";
						if ($(data).attr('tp') == 'esl') { // ajax element 객체
							prefix = $(data).attr('aid')+":";
						}
						resultXml += "<at did='"+$(data).attr('aid')+"' tp='12'/>";
						resultXml += "<e ei='"+ prefix + $(data).val()+"' emt='1'/>";
					}
				}

				xmlString += resultXml + "</es></mi></pa>";
			}
			// object prompt
			else if (name.indexOf('ob') > -1) {
				var resultXml = "<pa pt='6' pin='0' did='"+$(data).attr('pid')+"' tp='10'><mi><fct>";

				// check box
				if (name.indexOf('ck') > -1) {
					$(data).find('input[id^=chk_'+$(data).attr('pid')+']:checked').each(function(n) {
						var type = ($(this).attr('tp') == '4') ? 'mt' : 'at';
						resultXml += "<"+ type +" did='"+$(this).val()+"' tp='"+$(this).attr('tp')+"'/>";
					});
				}
				// radio
				else if (name.indexOf('rd') > -1) {
					var chkRd = $(data).find('input[name=rd_'+$(data).attr('pid')+']:checked');
					var type = ($(chkRd).attr('tp') == '4') ? 'mt' : 'at';
					resultXml += "<"+ type +" did='"+$(chkRd).val()+"' tp='"+$(chkRd).attr('tp')+"'/>";
				}
				// selectCheckbox
				else if (name.indexOf('sl') > -1) {  
					$(data).find('option[id^=sl_'+$(data).attr('pid')+']:checked').each(function(n) {
						var type = ($(this).attr('tp') == '4') ? 'mt' : 'at';
						resultXml += "<"+ type +" did='"+$(this).val()+"' tp='"+$(this).attr('tp')+"'/>";
					});
				}
				// combo box(select box)
				else {
					if ($(data).val() != "") { // 전체 선택이 아니면
						var selected = $(data).find('option:selected');
						var type = ($(selected).attr('tp') == '4') ? 'mt' : 'at';
						resultXml += "<"+ type +" did='"+$(data).val()+"' tp='"+$(selected).attr('tp')+"'/>";
					}
				}

				xmlString += resultXml + "</fct></mi></pa>";
			}
			// hierarchy prompt
			else if (name.indexOf('hi') > -1) {
				xmlString += $(data).getHierarchyAnswerXML();
			}
		});
	}
	//string replace
	return xmlString.replace(/&/gi, "&amp;");
};

/**
 * 프롬프트 답변 XML 형식 반환 (검색/다중선택)
 */
$.fn.getAnswerXMLForSearchElement = function(aid) {
	var frm = $(this);
	var xmlString = "";

	if ($(frm).find('input[type=checkbox]:checked').size() > 0) {
		xmlString += "<at did='"+aid+"' tp='12'/>";

		$(frm).find('input[type=checkbox]:checked').each(function(i) {
			xmlString += "<e ei='"+aid+":"+$(this).val()+"' emt='1'/>";
		});
	}

	return xmlString;
};


/**
 * 프롬프트 답변 XML 형식 반환 (계층구조)
 */
$.fn.getHierarchyAnswerXML = function() {
	var frm = $(this);
	var xmlString = "<pa pt='8' pin='0' did='"+$(this).attr('pid')+"' tp='10'><exp><nd et='14' nt='4' dmt='1' ddt='-1'>";

	$.each($(frm).find('select'), function (i, data) {
		var aid = $(data).attr('aid');

		if ($(data).find('option:selected').size() > 0) {
			if (!($(data).find('option:selected').size() == 1 && $(data).find('option:selected').val() == '')) {
				xmlString += "<nd et='5' nt='4' dmt='1' ddt='-1'><nd et='1' nt='5' dmt='1' ddt='-1'><at did='"+aid+"' tp='12'/></nd>";
				xmlString += "<nd et='1' nt='2' dmt='1' ddt='-1'><mi><es><at did='"+aid+"' tp='12'/>";

				$(data).find('option:selected').each(function(i) {
					xmlString += "<e ei='"+aid+":"+$(this).val()+"' emt='1' art='1'/>";
				});

				xmlString += "</es></mi></nd><op fnt='22'/></nd>";
			}
		}
	});

	xmlString += "<op fnt='19'/></nd></exp></pa>";
	return xmlString;
};

/**
 * 엑셀 내보내기
 */
function fn_excel(){
	if ($('iframe#ifr_brf').size() > 0) { // report
		fn_eventHandler($('iframe#ifr_brf').contents().find('#tbExport'));
	}
	else { // document
		fn_eventHandler($('iframe#ifr_brf').contents().find('iframe#promptFrm').contents().find('.tbExcel'));
	}
}


/**
 * 인쇄
 */
function fn_print(){
	if ($('iframe#ifr_brf').size() > 0) { // report
		fn_eventHandler($('iframe#ifr_brf').contents().find('#tbPrint'));
	}
	else { // document
		fn_eventHandler($('iframe#ifr_brf').contents().find('iframe#promptFrm').contents().find('.tbPrint'));
	}
}


/**
 * 페이지바이
 */
function fn_pageby(){
	if ($('iframe#ifr_brf').size() > 0) { // report
		fn_eventHandler($('iframe#ifr_brf').contents().find('#tbPageBy'));
	}
	else { // document
		fn_eventHandler($('iframe#ifr_brf').contents().find('iframe#promptFrm').contents().find('.tbPageBy'));
	}
}

/**
 * Button Event Handler
 * @param frm
 */
function fn_eventHandler(frm) {
	if ($(frm).length == 0) {
        alert('조회 후 사용하십시오.');
        return;
    }

	$(frm).click();
	return;
}
