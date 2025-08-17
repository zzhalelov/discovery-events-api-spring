package kz.zzhalelov.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

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