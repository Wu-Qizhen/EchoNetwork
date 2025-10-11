package com.wqz.echonetwork.entity.enums;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public enum ArticleStatus {
    DRAFT(0), // 草稿
    PUBLISHED(1), // 发布
    DELETED(2); // 删除

    private final int id;

    ArticleStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ArticleStatus fromId(int id) {
        for (ArticleStatus status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("未找到对应枚举：" + id);
    }
}
