package com.star.easydoc.service.git.model;

/**
 * 用户统计
 *
 * @author admin
 * @date 2023/12/23
 */
public class UserStats {
    /**
     * 增加
     */
    private int additions;
    /**
     * 删除
     */
    private int deletions;
    /**
     * 提交
     */
    private int commits; // 提交总数

    /**
     * 添加添加项
     *
     * @param count 计数
     */
    public void addAdditions(int count) {
        additions += count;
    }

    /**
     * 添加删除
     *
     * @param count 计数
     */
    public void addDeletions(int count) {
        deletions += count;
    }

    /**
     * 增量提交
     */
    public void incrementCommits() {
        commits++;
    }

    /**
     * 获取添加项
     *
     * @return int
     */
    public int getAdditions() {
        return additions;
    }

    /**
     * 获取删除内容
     *
     * @return int
     */
    public int getDeletions() {
        return deletions;
    }

    /**
     * 获取提交
     *
     * @return int
     */
    public int getCommits() {
        return commits;
    }
}