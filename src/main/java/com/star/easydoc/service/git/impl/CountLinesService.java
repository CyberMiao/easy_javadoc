package com.star.easydoc.service.git.impl;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.star.easydoc.service.git.GitService;
import com.star.easydoc.service.git.model.Lines;
import git4idea.commands.*;
import git4idea.repo.GitRepository;

import java.util.*;

import java.util.Map;

/**
 * 统计项目所有java文件的行数
 *
 * @author
 * @version 1.0
 */
public class CountLinesService implements GitService {

    /**
     * 记录
     */
    private static final Logger LOGGER = Logger.getInstance(CountLinesService.class);

    /**
     * 代码行map
     */
    private static Map<String, Lines> linesMap = new HashMap<>();

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
        GitLineHandler handler = new GitLineHandler(project, repository.getRoot(), GitCommand.LS_FILES);
        // 运行git ls-files命令
        GitCommandResult result = git.runCommand(handler);
        if (!result.success()) {
            // 处理命令执行失败的情况
            return;
        }
        linesMap = processResult(project, repository, result);
    }

    /**
     * 处理结果
     *
     * @param project    项目
     * @param repository 库
     * @param result     结果
     * @return map<字符串 、 行数>
     */
    @Override
    public Map<String, Lines> processResult(Project project, GitRepository repository, GitCommandResult result) {
        Map<String, Lines> linesMap = new HashMap<>();
        // 遍历每个文件并计算行数
        for (String file : result.getOutput()) {
            VirtualFile virtualFile = repository.getRoot().findFileByRelativePath(file);
            if (virtualFile != null && virtualFile.exists() && virtualFile.getFileType().getName().equals("JAVA")) {
                // 获取文件的行数
                Lines lines = getLinesCount(project, virtualFile);

                String fileName = virtualFile.getName();
                linesMap.put(fileName, linesMap.getOrDefault(fileName, new Lines(0, 0)).add(lines));
            }
        }
        return linesMap;
    }

    /**
     * 获取某文件的行数
     *
     * @param project     项目
     * @param virtualFile 虚拟文件
     * @return 行数
     */
    private Lines getLinesCount(Project project, VirtualFile virtualFile) {
        int totalLines = 0;
        int blankLines = 0;
        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        if (psiFile != null) {
            String[] lines = psiFile.getText().split("\n");
            totalLines = psiFile.getText().split("\n").length;
            for (String line : lines) {
                if (line.equals("")) {
                    blankLines ++;
                }
            }
        }
        return new Lines(totalLines, blankLines);
    }

    /**
     * 清空map
     */
    @Override
    public void clear() {
        linesMap.clear();
    }

    /**
     * 获取文件名称与行数的map
     *
     * @return map<字符串 、 行数>
     */
    public static Map<String, Lines> getLinesMap() {
        return linesMap;
    }

    /**
     * 设置map
     *
     * @param linesMap map
     */
    public static void setLinesMap(Map<String, Lines> linesMap) {
        CountLinesService.linesMap = linesMap;
    }
}
