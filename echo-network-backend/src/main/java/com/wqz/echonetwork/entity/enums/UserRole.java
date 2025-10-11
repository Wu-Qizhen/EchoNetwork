package com.wqz.echonetwork.entity.enums;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public enum UserRole {
    USER(0), // 普通用户
    SUPER_USER(1), // 超级用户
    ADMIN(3); // 管理员
    private final int id;

    UserRole(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UserRole fromId(int id) {
        for (UserRole role : values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("未找到对应枚举：" + id);
    }
}
