package com.star.easydoc.service.git.impl;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.star.easydoc.service.git.GitService;
import com.star.easydoc.service.git.model.UserStats;
import git4idea.commands.Git;
import git4idea.commands.GitCommand;
import git4idea.commands.GitCommandResult;
import git4idea.commands.GitLineHandler;
import git4idea.repo.GitRepository;

import java.util.*;

/**
 * 用户代码行数服务
 *
 * @author admin
 * @date 2023/12/23
 */
public class UserLinesService implements GitService {
    /**
     * 记录
     */
    private static final Logger LOGGER = Logger.getInstance(UserLinesService.class);

    /**
     * 用户统计信息 地图
     */
    private static Map<String, UserStats> userStatsMap = new HashMap<>();

    /**
     * do git 命令
     *
     * @param project    项目
     * @param repository 存储 库
     */
    @Override
    public void doGitCommand(Project project, GitRepository repository) {
        // 获取Git实例
        Git git = ServiceManager.getService(Git.class);

        // 使用GitLineHandler执行'git ls-files'命令获取所有被跟踪的文件列表
        GitLineHandler handler = new GitLineHandler(project, repository.getRoot(), GitCommand.LOG);
        handler.setSilent(false); // 可选，用于显示命令输出
        // 添加参数
        handler.addParameters("--format=%an", "--numstat");
        // 执行命令
        GitCommandResult result = git.runCommand(handler);
        if (!result.success()) {
            // 处理命令执行失败的情况
            return;
        }

        // 统计用户提交信息
        userStatsMap = processResult(project, repository, result);
    }

    /**
     * 处理结果
     *
     * @param project    项目
     * @param repository 存储 库
     * @param result     结果
     * @return map<字符串 、 用户统计信息>
     */
    @Override
    public Map<String, UserStats> processResult(Project project, GitRepository repository, GitCommandResult result) {
        Map<String, UserStats> userStatsMap = new HashMap<>();
        List<String> lines = result.getOutput();

        String currentUsername = null;
        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 1 && !parts[0].equals("")) {
                currentUsername = parts[0];
                UserStats userStats = userStatsMap.getOrDefault(currentUsername, new UserStats());
                userStats.incrementCommits();
                userStatsMap.put(currentUsername, userStats);
            }
            if (parts.length > 1) {
                try {
                    String filePath = parts[2];
                    // 只统计java代码
                    if (filePath.endsWith(".java")) {
                        int additions = Integer.parseInt(parts[0]);
                        int deletions = Integer.parseInt(parts[1]);
                        if (currentUsername != null && filePath.endsWith(".java")) {
                            UserStats userStats = userStatsMap.getOrDefault(currentUsername, new UserStats());
                            userStats.addAdditions(additions);
                            userStats.addDeletions(deletions);
                            userStatsMap.put(currentUsername, userStats);
                        }
                    }


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        return userStatsMap;
    }

    @Override
    public void clear() {
        userStatsMap.clear();
    }

    /**
     * @return map<字符串 、 用户统计信息>
     */
    public Map<String, UserStats> getUserStatsMap() {
        return userStatsMap;
    }

}
