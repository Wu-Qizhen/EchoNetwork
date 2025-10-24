package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.po.Tag;
import com.wqz.echonetwork.entity.vo.Result;
import com.wqz.echonetwork.service.TagService;
import com.wqz.echonetwork.service.impl.TagServiceImpl;
import com.wqz.echonetwork.utils.JsonUtil;
import com.wqz.echonetwork.utils.WriterUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
@WebServlet({
        "/api/tags/*",
})
public class TagController extends HttpServlet {

    private final TagService tagService = new TagServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Tag tag = JsonUtil.parseJson(request, Tag.class);

        if (tag == null || tag.getName().trim().isEmpty()) {
            WriterUtil.paramsError(response);
            return;
        }

        tag = tagService.create(tag.getName());

        if (tag == null) {
            Result<Object> result = Result.error("标签创建失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<Tag> result = Result.success("标签创建成功", tag);
        WriterUtil.writeJson(response, result);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String name = request.getParameter("name");
        String idStr = request.getParameter("id");
        String search = request.getParameter("search");

        Result<?> result;

        try {
            if (idStr != null && !idStr.trim().isEmpty()) {
                // 按 ID 查询
                Long id = Long.parseLong(idStr);
                Tag tag = tagService.getTagById(id);
                if (tag != null) {
                    result = Result.success("查询成功", tag);
                } else {
                    result = Result.error("未找到指定 ID 的标签");
                }
            } else if (name != null && !name.trim().isEmpty()) {
                Tag tag = tagService.getTagByName(name);
                if (tag != null) {
                    result = Result.success("查询成功", tag);
                } else {
                    result = Result.error("未找到指定名称的标签");
                }
            } else if (search != null && !search.trim().isEmpty()) {
                // 查询所有标签
                List<Tag> tags = tagService.search(search);
                result = Result.success("查询成功", tags);
            } else {
                result = Result.paramsError();
            }
        } catch (NumberFormatException e) {
            result = Result.error("ID 格式错误");
        } catch (Exception e) {
            result = Result.error("查询失败");
        }

        WriterUtil.writeJson(response, result);
    }
}
