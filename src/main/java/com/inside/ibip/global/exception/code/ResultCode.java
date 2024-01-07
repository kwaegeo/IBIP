package com.inside.ibip.global.exception.code;

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

    DUPLICATE_GROUP("E07", "중복된 사용자 혹은 사용자 그룹명입니다."),
    INVALID_GROUP_NAME("E08", "그룹명 형식에 맞게 작성해주세요."),

    INVALID_GROUP_ID("E09", "해당 그룹이 이미 삭제되었거나 존재하지 않습니다."),

    INVALID_USER_ID("E10", "해당 유저가 이미 삭제되었거나 존재하지 않습니다."),
    INVALID_PASSWORD_POLICY("E11", "비밀번호 정책에 부합하지 않습니다."),

    INVALID_USER_NAME("E12", "사용자명 형식에 맞게 작성해주세요."),

    INVALID_LOGIN_ID("E13", "사용자 로그인 ID 형식에 맞게 작성해주세요."),

    INVALID_PASSWORD("E14", "비밀번호와 비밀번호 확인이 같지 않습니다."),

    EXIST_LOGIN_ID("E15", "중복된 로그인 ID 입니다."),

    INVALID_DOCUMENT_ID("E16", "해당 리포트가 이미 삭제되었거나 존재하지 않습니다."),

    INVALID_TITLE("E17", "제목을 입력하지 않았습니다."),

    INVALID_CONTENT("E18", "내용을 입력하지 않았습니다"),

    MSTR_ETC_ERROR("M99","MSTR 연계 중 문제가 발생하였습니다. IntelligenceServer가  정상적으로 동작하는지 확인해주세요.");


    private final String code;
    private final String msg;

    }
