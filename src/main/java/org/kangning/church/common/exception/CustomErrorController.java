package org.kangning.church.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);

        Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)
        );

        int statusCode = (int) attributes.getOrDefault("status", 500);
        String message = (String) attributes.getOrDefault("message", "Unexpected error");

        String errorCode = mapStatusToErrorCode(statusCode);

        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(errorCode, message));
    }

    private String mapStatusToErrorCode(int statusCode) {
        return switch (HttpStatus.valueOf(statusCode)) {
            case NOT_FOUND -> "NOT_FOUND";                   // 404
            case FORBIDDEN -> "FORBIDDEN";                    // 403
            case UNAUTHORIZED -> "UNAUTHORIZED";              // 401
            case METHOD_NOT_ALLOWED -> "METHOD_NOT_ALLOWED";  // 405
            case BAD_REQUEST -> "BAD_REQUEST";                // 400
            case INTERNAL_SERVER_ERROR -> "INTERNAL_SERVER_ERROR"; // 500
            default -> "ERROR";                               // 其他錯誤
        };
    }
}