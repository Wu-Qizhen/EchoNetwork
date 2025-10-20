package com.wqz.echonetwork.entity.dto;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class UserRegisterRequest {
    private String username;
    private String email;
    private String password;

    public UserRegisterRequest() {
    }

    public UserRegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
                email != null && !email.trim().isEmpty() &&
                password != null && !password.trim().isEmpty() &&
                password.length() >= 6;
    }

    public String getValidationError() {
        if (username == null || username.trim().isEmpty()) {
            return "用户名不能为空";
        }
        if (email == null || email.trim().isEmpty()) {
            return "邮箱不能为空";
        }
        if (password == null || password.trim().isEmpty()) {
            return "密码不能为空";
        }
        if (password.length() < 6) {
            return "密码长度不能少于 6 位";
        }
        return null;
    }
}
