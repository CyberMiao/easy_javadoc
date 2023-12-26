package com.star.easydoc.view.settings.javadoc.template;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.star.easydoc.config.EasyDocConfig;
import com.star.easydoc.config.EasyDocConfig.TemplateConfig;
import com.star.easydoc.config.EasyDocConfigComponent;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @version 1.0.0
 * @since 2019-11-10 17:35:00
 */
public class MethodSettingsConfigurable extends AbstractTemplateConfigurable<MethodSettingsView> {
    private final EasyDocConfig config = ServiceManager.getService(EasyDocConfigComponent.class).getState();
    private final MethodSettingsView view = new MethodSettingsView(config);

    @Nls(capitalization = Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "EasyDocMethodTemplate";
    }

    // 获取配置展示的视图
    @Override
    public MethodSettingsView getView() {
        return view;
    }

    // 检查配置是否被修改
    @Override
    public boolean isModified() {
        TemplateConfig templateConfig = config.getMethodTemplateConfig();

        // 检查是否选择了默认模板的修改
        if (!Objects.equals(templateConfig.getIsDefault(), view.isDefault())) {
            return true;
        }

        // 检查自定义模板的修改
        return !Objects.equals(templateConfig.getTemplate(), view.getTemplate());
    }

    // 应用配置
    @Override
    public void apply() throws ConfigurationException {
        TemplateConfig templateConfig = config.getMethodTemplateConfig();

        // 更新是否选择默认模板的配置
        templateConfig.setIsDefault(view.isDefault());

        // 更新自定义模板的配置
        templateConfig.setTemplate(view.getTemplate());

        // 初始化自定义模板的 TreeMap
        if (templateConfig.getCustomMap() == null) {
            templateConfig.setCustomMap(new TreeMap<>());
        }

        // 检查自定义模板的正确性
        if (!view.isDefault()) {
            if (StringUtils.isBlank(view.getTemplate())) {
                throw new ConfigurationException("使用自定义模板，模板不能为空");
            }
            String temp = StringUtils.strip(view.getTemplate());
            if (!temp.startsWith("/**") || !temp.endsWith("*/")) {
                throw new ConfigurationException("模板格式不正确，正确的javadoc应该以\"/**\"开头，以\"*/\"结束");
            }
        }
    }

    // 重置配置
    @Override
    public void reset() {
        TemplateConfig templateConfig = config.getMethodTemplateConfig();

        // 设置是否选择默认模板
        view.setDefault(BooleanUtils.isTrue(templateConfig.getIsDefault()));

        // 设置自定义模板内容
        view.setTemplate(templateConfig.getTemplate());
    }
}
