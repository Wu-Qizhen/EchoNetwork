package com.wqz.echonetwork.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class Tag {

    private Long id;

    private String name;

    private Set<Long> articleIds = new HashSet<>();

}
