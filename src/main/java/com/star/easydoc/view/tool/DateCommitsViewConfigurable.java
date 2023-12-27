package com.star.easydoc.view.tool;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * 日期提交视图配置
 *
 * @author admin
 * @version  1.0
 */
public class DateCommitsViewConfigurable implements ToolWindowFactory {
    /**
     * 创建工具窗口内容
     *
     * @param project    项目
     * @param toolWindow 工具窗口
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        DateCommitView view = new DateCommitView();
        ContentFactory factory = ContentFactory.SERVICE.getInstance();
        Content content = factory.createContent(view.getMainPanel(), "Date Commit", false);
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * 是“启动时不激活”
     *
     * @return boolean
     */
    @Override
    public boolean isDoNotActivateOnStart() {
        return ToolWindowFactory.super.isDoNotActivateOnStart();
    }
}
