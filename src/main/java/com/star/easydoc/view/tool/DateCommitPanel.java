package com.star.easydoc.view.tool;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * “日期提交”面板
 *
 * @author admin
 * @date 2023/12/23
 */
public class DateCommitPanel extends JPanel {

    /**
     * 数据map
     */
    private Map<String, Integer> data;


    /**
     * “日期提交”面板
     *
     * @param data 数据
     */
    public DateCommitPanel(Map<String, Integer> data) {
        this.data = data;
    }

    /**
     * 画图组件
     * @param g g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null) {
            // 数据为空
            return;
        }
        int barWidth = getWidth() / (2 * data.size()); // 减小矩形宽度
        int maxValue = getMaxValue();

        // 画纵轴
        g.drawLine(50, 30, 50, getHeight() - 30);

        // 画横轴
        g.drawLine(50, getHeight() - 30, getWidth() - 30, getHeight() - 30);

        // 绘制横轴刻度和标签
        int i = data.size() - 1;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int x = 50 + 2 * i * barWidth; // 调整绘制位置
            g.drawString(entry.getKey(), x, getHeight() - 10);

            int barHeight = (int) (((double) entry.getValue() / maxValue) * (getHeight() - 60));
            int y = getHeight() - 30 - barHeight;

            g.setColor(JBColor.BLUE);
            g.fillRect(x, y, barWidth, barHeight);

            g.setColor(JBColor.BLACK);
            g.drawRect(x, y, barWidth, barHeight);

            // 在矩形上显示次数
            g.setColor(JBColor.BLACK);
            g.drawString(String.valueOf(entry.getValue()), x + barWidth / 2, y - 5);

            i--;
        }
    }

    /**
     * 获取最大value
     *
     * @return int
     */
    private int getMaxValue() {
        int max = Integer.MIN_VALUE;
        for (int value : data.values()) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
