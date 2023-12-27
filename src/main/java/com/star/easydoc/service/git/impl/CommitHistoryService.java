package com.star.easydoc.service.git.impl;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.star.easydoc.service.git.GitService;
import git4idea.commands.Git;
import git4idea.commands.GitCommand;
import git4idea.commands.GitCommandResult;
import git4idea.commands.GitLineHandler;
import git4idea.repo.GitRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 提交历史记录服务
 * 统计所有用户在每一天的提交量
 *
 * @author admin
 * @date 2023/12/23
 */
public class CommitHistoryService implements GitService {

    /**
     * 提交日志映射
     */
    private static Map<String, Map<String, Integer>> commitLogMap = new HashMap<>();

    private static final Logger LOGGER = Logger.getInstance(CommitHistoryService.class);

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

        // 使用GitLineHandler执行'git log'命令获取所有被跟踪的文件列表
        GitLineHandler handler = new GitLineHandler(project, repository.getRoot(), GitCommand.LOG);
        handler.addParameters("--format=\"%an %ad\"");
        // 运行git ls-files命令
        GitCommandResult result = git.runCommand(handler);
        if (!result.success()) {
            // 处理命令执行失败的情况
            return;
        }
        commitLogMap = processResult(project, repository, result);
    }

    /**
     * 处理结果
     *
     * @param project    项目
     * @param repository 库
     * @param result     结果
     * @return map<字符串 、 map < 字符串 、 整数>>
     */
    @Override
    public Map<String, Map<String, Integer>> processResult(Project project, GitRepository repository, GitCommandResult result) {
        Map<String, Map<String, Integer>> commitLogMap = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.ENGLISH);

        for (String entry : result.getOutput()) {
            String[] parts = entry.split("\\s+", 6);
            if (parts.length == 6) {
                String author = parts[0];
                String dateString = parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4] + " " + parts[5];

                try {
                    Date date = dateFormat.parse(dateString);
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

                    // 初始化作者的提交次数Map
                    commitLogMap.putIfAbsent(author, new LinkedHashMap<>());

                    // 更新作者在该日期的提交次数
                    commitLogMap.get(author).merge(formattedDate, 1, Integer::sum);
                } catch (ParseException e) {
                    LOGGER.error("结果格式错误");
                }
            }
        }
        return commitLogMap;
    }

    /**
     * 清空map
     */
    @Override
    public void clear() {
        commitLogMap.clear();
    }

    /**
     * 获取提交日志映射
     *
     * @return map<字符串 、 map < 字符串 、 整数>>
     */
    public Map<String, Map<String, Integer>> getCommitLogMap() {
        return commitLogMap;
    }

}
