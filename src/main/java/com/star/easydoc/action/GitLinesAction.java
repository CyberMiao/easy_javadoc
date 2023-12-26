package com.star.easydoc.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.star.easydoc.service.git.impl.CommitHistoryService;
import com.star.easydoc.service.git.impl.CountLinesService;
import com.star.easydoc.service.git.impl.UserLinesService;
import git4idea.GitUserRegistry;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;

import java.util.*;

/**
 * Git Lines 操作
 *
 * @author admin
 * @date 2023/12/23
 */
public class GitLinesAction extends AnAction {

    /**
     * 统计代码行数服务
     */
    private CountLinesService linesService = ServiceManager.getService(CountLinesService.class);

    /**
     * 统计用户代码行数服务
     */
    private UserLinesService userLinesService = ServiceManager.getService(UserLinesService.class);

    /**
     * 提交历史记录服务
     */
    private CommitHistoryService commitHistoryService = new CommitHistoryService();

    /**
     * 执行操作
     *
     * @param e e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        // 通知
        NotificationGroup notificationGroup = new NotificationGroup("easy_javadoc", NotificationDisplayType.BALLOON, false);
        Notification notification;

        Project project = e.getProject();
        if (project == null) {
            return;
        }
        // 获取仓库管理
        GitRepositoryManager manager = GitRepositoryManager.getInstance(project);
        List<GitRepository> gitRepositories = manager.getRepositories();
        if (gitRepositories.isEmpty()) {
            // 仓库为空，项目没有使用git
            notification = notificationGroup.createNotification("该项目未使用git。", MessageType.WARNING);
            // 清空map
            linesService.clear();
            userLinesService.clear();
            commitHistoryService.clear();
        } else {
            for (GitRepository repo : gitRepositories) {
                // 统计代码总数
                linesService.doGitCommand(project, repo);
                // 统计用户提交信息
                userLinesService.doGitCommand(project, repo);
                // 统计用户提交次数
                commitHistoryService.doGitCommand(project, repo);
            }
            notification = notificationGroup.createNotification("成功！！", MessageType.INFO);
        }

        Notifications.Bus.notify(notification);
    }
}
