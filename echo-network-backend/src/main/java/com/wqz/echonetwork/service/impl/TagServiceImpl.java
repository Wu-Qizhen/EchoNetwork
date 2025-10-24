
package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.po.Tag;
import com.wqz.echonetwork.mapper.TagMapper;
import com.wqz.echonetwork.service.TagService;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class TagServiceImpl implements TagService {

    TagMapper tagMapper = new TagMapper();

    @Override
    public Tag create(String name) {
        Tag tag = tagMapper.findByName(name);
        if (tag == null) {
            tag = new Tag(null, name);
            long insert = tagMapper.insert(tag);
            if (insert > 0) {
                tag.setId(insert);
                return tag;
            } else {
                return null;
            }
        }
        return tag;
    }

    @Override
    public Tag getTagByName(String name) {
        if (tagMapper.existsByName(name)) {
            return tagMapper.findByName(name);
        } else {
            return null;
        }
    }

    @Override
    public Tag getTagById(Long id) {
        Tag tag = tagMapper.findById(id);
        if (tag != null && tag.getId() != null) {
            return tag;
        } else {
            return null;
        }
    }

    @Override
    public List<Tag> search(String name) {
        return tagMapper.search(name);
    }
}
