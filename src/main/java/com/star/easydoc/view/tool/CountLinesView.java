package com.star.easydoc.view.tool;

import com.intellij.openapi.diagnostic.Logger;
import com.star.easydoc.service.git.impl.CountLinesService;
import com.star.easydoc.service.git.model.Lines;
import com.star.easydoc.service.git.impl.UserLinesService;
import com.star.easydoc.service.git.model.UserStats;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

/**
 * 右侧统计工具栏视图
 *
 * @author
 * @version 1.0
 */
public class CountLinesView {

    /**
     * 代码行数列
     */
    private String[] linesColumns = {"文件名", "总行数", "空格数", "空格数占比"};
    /**
     * 提交列
     */
    private String[] commitColumn = {"用户名", "提交数", "增加代码行数", "减少代码行数"};
    /**
     * 主面板
     */
    private JPanel mainPanel;
    /**
     * 内容面板
     */
    private JPanel contentPanel;
    /**
     * 按钮面板
     */
    private JPanel buttonPanel;
    /**
     * 刷新按钮
     */
    private JButton refresh;
    /**
     * 代码行信息
     */
    private JLabel lineInfo;
    /**
     * 滚动窗格
     */
    private JScrollPane scrollPane;
    /**
     * Lines 表格
     */
    private JTable linesTable;

    /**
     * 提交面板
     */
    private JPanel commitPanel;
    /**
     *  提交按钮面板
     */
    private JPanel cButtonPanel;
    /**
     * 提交滚动窗格
     */
    private JScrollPane cScrollPane;
    /**
     * 提交表
     */
    private JTable commitTable;
    private JPanel westP;
    private JPanel eastP;
    private JLabel count;
    private JLabel commit;
    private static final Logger LOGGER = Logger.getInstance(CountLinesView.class);

    /**
     * 行计数统计视图
     */
    public CountLinesView() {
        initComponents();
        //给刷新按钮添加事件监听
        refresh.addActionListener(e -> {
            // 获取统计后的代码量
            Map<String, Lines> linesMap = CountLinesService.getLinesMap();
            DefaultTableModel model = (DefaultTableModel) linesTable.getModel();
            model.setDataVector(getLinesContent(linesMap), linesColumns);
            Map<String, UserStats> userStatsMap = UserLinesService.getUserStatsMap();
            DefaultTableModel userModel = (DefaultTableModel) commitTable.getModel();
            userModel.setDataVector(getUserLinesContent(userStatsMap), commitColumn);

            // 刷新表格
            linesTable.revalidate();
            linesTable.repaint();
            commitTable.revalidate();
            commitTable.repaint();
        });
    }


    /**
     * init 组件
     */
    private void initComponents() {
        initTable();
    }


    /**
     * init 表
     */
    private void initTable() {
        Map<String, Lines> linesMap = CountLinesService.getLinesMap();
        Map<String, UserStats> userStatsMap = UserLinesService.getUserStatsMap();

        // 使用 DefaultTableModel 替代二维数组
        DefaultTableModel model = new DefaultTableModel(getLinesContent(linesMap), linesColumns);
        DefaultTableModel userModel = new DefaultTableModel(getUserLinesContent(userStatsMap), commitColumn);

        linesTable.setModel(model);
        commitTable.setModel(userModel);
    }

    // 辅助方法，将 Map 转换为二维数组
    private Object[][] getLinesContent(Map<String, Lines> linesMap) {
        Object[][] content = new Object[linesMap.size()+1][4];
        try {
            int i = 0;
            int total = 0;
            int blank = 0;
            for (Map.Entry<String, Lines> entry : linesMap.entrySet()) {
                String fileName = entry.getKey();
                Lines lines = entry.getValue();
                content[i][0] = fileName;
                content[i][1] = lines.getTotalLines();
                content[i][2] = lines.getBlankLines();
                content[i][3] = lines.getBlankLines() / (double) lines.getTotalLines();
                total += lines.getTotalLines();
                blank += lines.getBlankLines();
                i++;
            }
            content[i][0] = "Total";
            content[i][1] = total;
            content[i][2] = blank;
            content[i][3] = (double) blank / (total == 0 ? 1 : total);
        }
        catch (Exception e) {
            LOGGER.error("统计错误");
        }

        return content;
    }


    /**
     * 获取用户行内容
     *
     * @param userStatsMap 用户统计信息 表
     * @return 对象[][]
     */
    private Object[][] getUserLinesContent(Map<String, UserStats> userStatsMap) {
        Object[][] content = new Object[userStatsMap.size()][4];
        int i = 0;
        for (Map.Entry<String, UserStats> entry : userStatsMap.entrySet()) {
            String userName = entry.getKey();
            UserStats userStats = entry.getValue();
            content[i][0] = userName;
            content[i][1] = userStats.getCommits();
            content[i][2] = userStats.getAdditions();
            content[i][3] = userStats.getDeletions();
            i++;
        }

        return content;
    }

    /**
     * 获取主面板
     *
     * @return jpanel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Get 组件
     *
     * @return JConmitt
     */
    public JComponent getComponent() {
        return mainPanel;
    }
}