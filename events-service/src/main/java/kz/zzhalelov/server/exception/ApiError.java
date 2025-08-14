package kz.zzhalelov.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApiError {
    private String reason;
    private String message;
    private List<String> errors;
    private String status;
    private String timestamp;
}