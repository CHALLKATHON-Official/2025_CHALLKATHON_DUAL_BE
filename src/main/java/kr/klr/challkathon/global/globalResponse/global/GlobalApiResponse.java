package kr.klr.challkathon.global.globalResponse.global;

import lombok.Getter;

@Getter
public class GlobalApiResponse<T> {
    private final T data;
    private final String message;
    private final String status;
    private final String action;

    private GlobalApiResponse(T data, String message, String status, String action) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.action = action;
    }

    public static <T> GlobalApiResponse<T> success() {
        return new GlobalApiResponse<>(null, null, "SUCCESS", null);
    }

    public static <T> GlobalApiResponse<T> success(String message) {
        return new GlobalApiResponse<>(null, message, "SUCCESS", null);
    }

    public static <T> GlobalApiResponse<T> success(T data) {
        return new GlobalApiResponse<>(data, null, "SUCCESS", null);
    }

    public static <T> GlobalApiResponse<T> success(T data, String message) {
        return new GlobalApiResponse<>(data, message, "SUCCESS", null);
    }

    public static <T> GlobalApiResponse<T> success(T data, String message, String action) {
        return new GlobalApiResponse<>(data, message, "SUCCESS", action);
    }

    public static <T> GlobalApiResponse<T> error(String message) {
        return new GlobalApiResponse<>(null, message, "ERROR", null);
    }

    public static <T> GlobalApiResponse<T> error(T data, String message) {
        return new GlobalApiResponse<>(data, message, "ERROR", null);
    }

    public static <T> GlobalApiResponse<T> error(T data, String message, String status) {
        return new GlobalApiResponse<>(data, message, status, null);
    }
    public static <T> GlobalApiResponse<T> error(T data, String message, String status, String action) {
        return new GlobalApiResponse<>(data, message, status, action);
    }
} 