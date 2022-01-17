package com.github.bourbon.springframework.boot.autoconfigure;

import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.SetUtils;

import java.util.EventObject;
import java.util.List;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/18 23:59
 */
public class AutoConfigurationImportEvent extends EventObject {

    private static final long serialVersionUID = -2038905087652478533L;

    private final List<String> candidateConfigurations;

    private final Set<String> exclusions;

    public AutoConfigurationImportEvent(Object source, List<String> candidateConfigurations, Set<String> exclusions) {
        super(source);
        this.candidateConfigurations = ListUtils.unmodifiableList(candidateConfigurations);
        this.exclusions = SetUtils.unmodifiableSet(exclusions);
    }

    public List<String> getCandidateConfigurations() {
        return candidateConfigurations;
    }

    public Set<String> getExclusions() {
        return exclusions;
    }
}