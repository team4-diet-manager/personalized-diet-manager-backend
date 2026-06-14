package com.pdm.dietmanager.dto.response;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Schema(description = "공통 오류 응답")
public class ErrorResponse {
    @Schema(description = "HTTP 상태 코드", example = "400")
    private final int status;

    @Schema(description = "HTTP 오류 이름", example = "BAD_REQUEST")
    private final String error;

    @Schema(description = "오류 메시지", example = "입력값을 확인해주세요.")
    private final String message;

    @Schema(description = "필드별 검증 오류 목록")
    private final List<FieldErrorResponse> fieldErrors;

    private ErrorResponse(
            int status,
            String error,
            String message,
            List<FieldErrorResponse> fieldErrors
    ) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(status.value(), status.name(), message, List.of());
    }

    public static ErrorResponse of(
            HttpStatus status,
            String message,
            List<FieldErrorResponse> fieldErrors
    ) {
        return new ErrorResponse(status.value(), status.name(), message, fieldErrors);
    }

    @Getter
    public static class FieldErrorResponse {
        @Schema(description = "오류가 발생한 필드명", example = "age")
        private final String field;

        @Schema(description = "필드 오류 메시지", example = "나이는 1 이상이어야 합니다.")
        private final String message;

        public FieldErrorResponse(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
