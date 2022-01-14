package com.github.bourbon.base.extension.model;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 21:00
 */
public class ModuleModel extends ScopeModel {

    private static final ModuleModel defaultInstance = new ModuleModel(ApplicationModel.defaultModel());

    public static ModuleModel defaultModel() {
        return defaultInstance;
    }

    public static ModuleModel of(ApplicationModel applicationModel) {
        return new ModuleModel(applicationModel);
    }

    private final ApplicationModel applicationModel;

    public ModuleModel(ApplicationModel applicationModel) {
        super(applicationModel, ExtensionScope.MODULE);
        this.applicationModel = ObjectUtils.requireNonNull(applicationModel, "ApplicationModel can not be null");
        this.applicationModel.addModule(this);
    }

    public ApplicationModel getApplicationModel() {
        return applicationModel;
    }
}