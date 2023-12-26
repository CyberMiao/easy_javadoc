package com.star.easydoc.service.git.model;
public class Lines {
    /**
     * 总行数
     */
    private int totalLines;
    /**
     * 空白行
     */
    private int blankLines;

    /**
     * 线
     *
     * @param totalLines 总行数
     * @param blankLines 空白行
     */
    public Lines(int totalLines, int blankLines) {
        this.totalLines = totalLines;
        this.blankLines = blankLines;
    }

    /**
     * 添加
     *
     * @param lines 行
     * @return lines
     */
    public Lines add(Lines lines) {
        this.totalLines += lines.getTotalLines();
        this.blankLines += lines.getBlankLines();
        return this;
    }

    /**
     * 获取总行数
     *
     * @return int
     */
    public int getTotalLines() {
        return totalLines;
    }

    /**
     * 设置总行数
     *
     * @param totalLines 总行数
     */
    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }

    /**
     * 获取空行
     *
     * @return int
     */
    public int getBlankLines() {
        return blankLines;
    }

    /**
     * 设置空行
     *
     * @param blankLines 空白行
     */
    public void setBlankLines(int blankLines) {
        this.blankLines = blankLines;
    }

    /**
     * 到字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "{TotalLines: " + totalLines + " BlankLines:" + blankLines + "}";
    }
}