package com.github.bourbon.base.extension.model;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 15:40
 */
public interface ScopeModelAccessor {

    ScopeModel getScopeModel();

    default FrameworkModel getFrameworkModel() {
        return ScopeModelUtils.getFrameworkModel(getScopeModel());
    }

    default ApplicationModel getApplicationModel() {
        return ScopeModelUtils.getApplicationModel(getScopeModel());
    }

    default ModuleModel getModuleModel() {
        return ScopeModelUtils.getModuleModel(getScopeModel());
    }
}