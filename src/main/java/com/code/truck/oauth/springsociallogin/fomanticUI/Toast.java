package com.code.truck.oauth.springsociallogin.fomanticUI;

public record Toast(
        String title,
        String message,
        String clazz) {

    public static Toast success(String title, String message) {
        return new Toast(title, message, "success");
    }

    public static Toast warning(String title, String message) {
        return new Toast(title, message, "warning");
    }

    public static Toast error(String title, String message) {
        return new Toast(title, message, "error");
    }
}
