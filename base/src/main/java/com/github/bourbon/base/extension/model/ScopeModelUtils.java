package com.github.bourbon.base.extension.model;

import com.github.bourbon.base.extension.annotation.SPI;
import com.github.bourbon.base.extension.support.ExtensionLoader;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 21:02
 */
public interface ScopeModelUtils {

    static ScopeModel getOrDefault(ScopeModel scopeModel, Class<?> type) {
        return ObjectUtils.defaultSupplierIfNull(scopeModel, () -> getDefaultScopeModel(type));
    }

    static ScopeModel getDefaultScopeModel(Class<?> type) {
        SPI spi = type.getAnnotation(SPI.class);
        if (spi == null) {
            throw new IllegalArgumentException("SPI annotation not found for class: " + type.getName());
        }
        switch (spi.scope()) {
            case FRAMEWORK:
                return FrameworkModel.defaultModel();
            case APPLICATION:
                return ApplicationModel.defaultModel();
            case MODULE:
                return ModuleModel.defaultModel();
            default:
                throw new IllegalStateException("Unable to get default scope model for type: " + type.getName());
        }
    }

    static FrameworkModel getFrameworkModel(ScopeModel scopeModel) {
        if (scopeModel == null) {
            return FrameworkModel.defaultModel();
        }
        if (scopeModel instanceof FrameworkModel) {
            return (FrameworkModel) scopeModel;
        }
        if (scopeModel instanceof ApplicationModel) {
            return ((ApplicationModel) scopeModel).getFrameworkModel();
        }
        if (scopeModel instanceof ModuleModel) {
            return ((ModuleModel) scopeModel).getApplicationModel().getFrameworkModel();
        }
        throw new IllegalArgumentException("Unable to get FrameworkModel from " + scopeModel);
    }

    static ApplicationModel getApplicationModel(ScopeModel scopeModel) {
        if (scopeModel == null) {
            return ApplicationModel.defaultModel();
        }
        if (scopeModel instanceof ApplicationModel) {
            return (ApplicationModel) scopeModel;
        }
        if (scopeModel instanceof ModuleModel) {
            return ((ModuleModel) scopeModel).getApplicationModel();
        }
        throw new IllegalArgumentException("Unable to get ApplicationModel from " + scopeModel);
    }

    static ModuleModel getModuleModel(ScopeModel scopeModel) {
        if (scopeModel == null) {
            return ModuleModel.defaultModel();
        }
        if (scopeModel instanceof ModuleModel) {
            return (ModuleModel) scopeModel;
        }
        throw new IllegalArgumentException("Unable to get ModuleModel from " + scopeModel);
    }

    static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return getExtensionLoader(type, null);
    }

    static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type, ScopeModel scopeModel) {
        if (scopeModel != null) {
            return scopeModel.getExtensionLoader(type);
        }
        SPI spi = type.getAnnotation(SPI.class);
        if (spi == null) {
            throw new IllegalArgumentException("SPI annotation not found for class: " + type.getName());
        }
        switch (spi.scope()) {
            case FRAMEWORK:
                return FrameworkModel.defaultModel().getExtensionLoader(type);
            case APPLICATION:
                return ApplicationModel.defaultModel().getExtensionLoader(type);
            case MODULE:
                return ModuleModel.defaultModel().getExtensionLoader(type);
            default:
                throw new IllegalArgumentException("Unable to get ExtensionLoader for type: " + type.getName());
        }
    }
}