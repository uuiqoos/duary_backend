package com.ivis.duary.config.response;

public enum ResponseCode {
    //요청 성공
    OK(200, "OK", "요청 성공"),

    //Global Server Error
    INTERNAL_SERVER_ERROR(500,  "Internal Server Error", "의도 하지 않은 서버에러 발생"),

    //인증 만료
    UNAUTHORIZED(401, "unauthorized", "인증키 필요"),

    WRONG_REQUEST(400, "wrong request", "잘못된 요청입니다"),

    NO_USER_FOUND(401, "No user found", "아이디 혹은 비밀번호가 잘못되었습니다"),

    DUPLICATE_USERNAME(400, "Duplicate username", "이미 존재하는 아이디입니다"),

    //권한 없음
    FORBIDDEN(403,  "Forbidden", "권한 없음"),

    //데이터 찾을수 없을경우 사용
    DATA_NOT_FOUND(500,  "데이터를 찾을 수 없습니다.", "데이터를 찾을수 없는 경우");

    private final int status;
    private final String message;
    private final String descr;

    ResponseCode(int status, String message, String descr) {
        this.status = status;
        this.message = message;
        this.descr = descr;
    }
    public int getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public String getDescr() {
        return descr;
    }

}
