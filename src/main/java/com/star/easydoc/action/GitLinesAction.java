package com.star.easydoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
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
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        // 获取仓库管理
        GitRepositoryManager manager = GitRepositoryManager.getInstance(project);
        List<GitRepository> gitRepositories = manager.getRepositories();

        for (GitRepository repo : gitRepositories) {
            // 统计代码总数
            linesService.doGitCommand(project, repo);
            // 统计用户提交信息
            userLinesService.doGitCommand(project, repo);
            // 统计用户提交次数
            commitHistoryService.doGitCommand(project, repo);
        }
    }
}
