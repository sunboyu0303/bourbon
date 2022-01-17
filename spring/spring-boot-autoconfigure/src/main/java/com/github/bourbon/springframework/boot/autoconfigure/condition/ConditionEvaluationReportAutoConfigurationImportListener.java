package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.springframework.boot.autoconfigure.AutoConfigurationImportEvent;
import com.github.bourbon.springframework.boot.autoconfigure.AutoConfigurationImportListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/22 17:02
 */
class ConditionEvaluationReportAutoConfigurationImportListener implements AutoConfigurationImportListener, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event) {
        if (beanFactory != null) {
            ConditionEvaluationReport report = ConditionEvaluationReport.get(beanFactory);
            report.recordEvaluationCandidates(event.getCandidateConfigurations());
            report.recordExclusions(event.getExclusions());
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = BooleanUtils.defaultIfAssignableFrom(beanFactory, ConfigurableListableBeanFactory.class, ConfigurableListableBeanFactory.class::cast);
    }
}