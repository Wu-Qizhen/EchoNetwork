package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.Tag;
import com.wqz.echonetwork.utils.SqlUtil;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.22
 */
public class TagMapper {
    public Tag findById(Long id) {
        return SqlUtil.queryObject(
                "SELECT * FROM tag WHERE id = ?",
                Tag.class,
                id
        );
    }

    public Tag findByName(String name) {
        return SqlUtil.queryObject(
                "SELECT * FROM tag WHERE name = ?",
                Tag.class,
                name
        );
    }

    public List<Tag> findAll() {
        return SqlUtil.queryList(
                "SELECT * FROM tag ORDER BY name",
                Tag.class
        );
    }

    public List<Tag> search(String keyword) {
        return SqlUtil.queryList(
                "SELECT * FROM tag WHERE name LIKE ?",
                Tag.class,
                "%" + keyword + "%"
        );
    }

    public long insert(Tag tag) {
        return SqlUtil.insert(
                "INSERT INTO tag (name) VALUES (?)",
                tag.getName()
        );
    }

    public int update(Tag tag) {
        return SqlUtil.update(
                "UPDATE tag SET name = ? WHERE id = ?",
                tag.getName(),
                tag.getId()
        );
    }

    public int delete(Long id) {
        return SqlUtil.update("DELETE FROM tag WHERE id = ?", id);
    }

    public boolean existsByName(String name) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM tag WHERE name = ?",
                Integer.class,
                name
        );
        return count != null && count > 0;
    }
}
