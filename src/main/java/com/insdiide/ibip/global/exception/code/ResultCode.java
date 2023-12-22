package com.insdiide.ibip.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultCode {
    SUCCESS("S00", "성공입니다."),
    ID_MISSING("E01", "아이디를 입력하지 않았습니다."),
    PASSWORD_MISSING("E02","비밀번호를 입력하지 않았습니다."),
    ETC_ERROR("E99", "기타 에러가 발생했습니다."),
    INCORRECT_USERNAME_OR_PASSWORD("M01", "입력하신 아이디와 패스워드에 해당하는 사용자가 없습니다."),
    UNREGISTERED_PROJECT("M02", "등록되지 않은 프로젝트입니다."),
    UNREGISTERED_SERVER_NAME("M03","등록되지 않은 서버입니다. 연결을 다시 확인해주세요."),
    MSTR_NO_SESSION("M04", "MSTR 세션이 만료되었습니다."),

    NO_TEMPLATE_NAME("E03", "템플릿 명을 입력해주세요."),
    EXISTS_TEMPLATE_NAME("E04", "중복된 템플릿명 입니다."),

    INVALID_REMARK("E05", "설명 유효길이를 초과 하였습니다. [200자]"),

    ERROR_ADD_USER("E06", "사용자 추가 중 에러가 발생하였습니다."),

    MSTR_ETC_ERROR("M99","MSTR 연계 중 문제가 발생하였습니다.");

    private final String code;
    private final String msg;

    }
