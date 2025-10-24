package com.wqz.echonetwork.service;

import com.wqz.echonetwork.entity.po.Tag;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public interface TagService {

    Tag create(String name);

    Tag getTagByName(String name);

    Tag getTagById(Long id);

    List<Tag> search(String name);
}
