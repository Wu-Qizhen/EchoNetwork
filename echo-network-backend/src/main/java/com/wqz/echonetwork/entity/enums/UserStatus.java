package com.wqz.echonetwork.entity.enums;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public enum UserStatus {
    NORMAL(0), // 正常
    BANNED(1), // 封禁
    DELETED(2); // 已删除

    private final int id;

    UserStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UserStatus fromId(int id) {
        for (UserStatus status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("未找到对应枚举：" + id);
    }
}
