package com.github.bourbon.base.extension.model;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.SetUtils;

import java.util.List;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 20:40
 */
public class FrameworkModel extends ScopeModel {

    private static final List<FrameworkModel> allInstances = ListUtils.synchronizedList(ListUtils.newArrayList());
    private static final FrameworkModel defaultInstance = new FrameworkModel();

    public static FrameworkModel defaultModel() {
        return defaultInstance;
    }

    public static FrameworkModel of() {
        return new FrameworkModel();
    }

    private final Set<ApplicationModel> applicationModels = SetUtils.synchronizedSet(SetUtils.newHashSet());

    private FrameworkModel() {
        super(null, ExtensionScope.FRAMEWORK);
        FrameworkModel.allInstances.add(this);
    }

    void addApplication(ApplicationModel model) {
        this.applicationModels.add(model);
    }
}