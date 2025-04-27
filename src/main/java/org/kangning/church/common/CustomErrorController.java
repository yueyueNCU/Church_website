package org.kangning.church.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
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
        // 抓取錯誤細節
        WebRequest webRequest = new ServletWebRequest(request);

        Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                webRequest, ErrorAttributeOptions.of(
                        ErrorAttributeOptions.Include.MESSAGE
                )
        );

        // 包成自己定義的 ErrorResponse
        return ResponseEntity
                .status((int) attributes.getOrDefault("status", 500))
                .body(new ErrorResponse(
                        "ERROR",
                        (String) attributes.getOrDefault("message", "Unexpected error")
                ));
    }
}