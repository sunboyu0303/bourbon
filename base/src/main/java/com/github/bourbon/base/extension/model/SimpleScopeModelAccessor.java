package com.github.bourbon.base.extension.model;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 15:39
 */
public class SimpleScopeModelAccessor implements ScopeModelAccessor {

    private final ScopeModel scopeModel;
    private FrameworkModel frameworkModel;
    private ApplicationModel applicationModel;
    private ModuleModel moduleModel;

    public SimpleScopeModelAccessor(ScopeModel scopeModel) {
        this.scopeModel = scopeModel;
        if (scopeModel instanceof FrameworkModel) {
            this.frameworkModel = (FrameworkModel) scopeModel;
        } else if (scopeModel instanceof ApplicationModel) {
            this.applicationModel = (ApplicationModel) scopeModel;
            this.frameworkModel = applicationModel.getFrameworkModel();
        } else if (scopeModel instanceof ModuleModel) {
            this.moduleModel = (ModuleModel) scopeModel;
            this.applicationModel = moduleModel.getApplicationModel();
            this.frameworkModel = applicationModel.getFrameworkModel();
        }
    }

    @Override
    public ScopeModel getScopeModel() {
        return scopeModel;
    }

    @Override
    public FrameworkModel getFrameworkModel() {
        return frameworkModel;
    }

    @Override
    public ApplicationModel getApplicationModel() {
        return applicationModel;
    }

    @Override
    public ModuleModel getModuleModel() {
        return moduleModel;
    }
}