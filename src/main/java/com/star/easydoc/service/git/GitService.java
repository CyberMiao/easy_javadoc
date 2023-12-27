package com.star.easydoc.service.git;

import com.intellij.openapi.project.Project;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRepository;

import java.util.Map;

/**
 * Git 服务
 *
 * @author
 * @date 2023/12/23
 */
public interface GitService {

    /**
     * do git 命令
     *
     * @param project    项目
     * @param repository 存储 库
     */
    void doGitCommand(Project project, GitRepository repository);

    /**
     * 处理结果
     *
     * @param project    项目
     * @param repository 存储 库
     * @param result     结果
     * @return 地图
     */
    Map processResult(Project project, GitRepository repository, GitCommandResult result);

    /**
     * 清空数据
     */
    void clear();

}
