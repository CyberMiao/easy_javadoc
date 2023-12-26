package com.star.easydoc.view.tool;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.util.ui.CalendarView;
import com.michaelbaranov.microba.calendar.CalendarPane;
import com.star.easydoc.service.git.impl.CommitHistoryService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 日期提交视图
 *
 * @author admin
 * @date 2023/12/23
 */
public class DateCommitView {
    /**
     * 主组件
     */
    private JPanel mainP;
    /**
     * “搜索”按钮
     */
    private JButton searchButton;
    /**
     * 作者姓名
     */
    private JComboBox<String> authorsName;
    /**
     * “搜索”面板
     */
    private JPanel searchPanel;
    /**
     * 左组件
     */
    private JPanel eastP;
    /**
     * 右组件
     */
    private JPanel westP;
    /**
     * 提交日期组件
     */
    private DateCommitPanel dataP;
    /**
     * 底部组件
     */
    private JPanel bottomP;


    /**
     * 提交历史记录服务
     */
    private CommitHistoryService commitHistoryService = ServiceManager.getService(CommitHistoryService.class);
    /**
     * 提交表
     */
    private Map<String, Map<String, Integer>> commitsMap = new HashMap<>();

    /**
     * 提交日期视图
     */
    public DateCommitView() {

        //给按钮加上动作监听
        searchButton.addActionListener(new ActionListener() {
            /**
             * 执行操作
             *
             * @param e e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                if (commitsMap.isEmpty()) {
                    authorsName.removeAllItems();
                    authorsName.revalidate();
                    authorsName.repaint();
                }
                if (authorsName.getItemCount() == 0) {
                    // 为空时，刷新
                    commitsMap = commitHistoryService.getCommitLogMap();
                    init();
                    authorsName.revalidate();
                    authorsName.repaint();
                }

                // 获取选中用户名的用户提交信息
                String author = (String) authorsName.getSelectedItem();
                Map<String, Integer> userCommits = commitsMap.get(author);
                if (dataP != null) {
                    mainP.remove(dataP);
                }
                dataP = new DateCommitPanel(userCommits);
                mainP.add(dataP);
                mainP.revalidate();
                mainP.repaint();
            }
        });

    }

    /**
     * 获取主面板
     *
     * @return JConmitt
     */
    public JComponent getMainPanel() {
        return mainP;
    }

    /**
     * 初始化
     */
    private void init() {
        Set<String> authors = commitHistoryService.getCommitLogMap().keySet();
        for (String author : authors) {
            authorsName.addItem(author);
        }
    }

}
