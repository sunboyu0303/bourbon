package com.github.bourbon.base.extension.model;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 20:37
 */
public class ApplicationModel extends ScopeModel {

    private static final ApplicationModel defaultInstance = new ApplicationModel(FrameworkModel.defaultModel());

    public static ApplicationModel defaultModel() {
        return defaultInstance;
    }

    public static ApplicationModel of(FrameworkModel frameworkModel) {
        return new ApplicationModel(frameworkModel);
    }

    private final FrameworkModel frameworkModel;

    private final Set<ModuleModel> moduleModels = SetUtils.synchronizedSet(SetUtils.newHashSet());

    private ApplicationModel(FrameworkModel frameworkModel) {
        super(frameworkModel, ExtensionScope.APPLICATION);
        this.frameworkModel = ObjectUtils.requireNonNull(frameworkModel, "FrameworkModel can not be null");
        this.frameworkModel.addApplication(this);
    }

    public FrameworkModel getFrameworkModel() {
        return frameworkModel;
    }

    public void addModule(ModuleModel moduleModel) {
        moduleModels.add(moduleModel);
    }
}