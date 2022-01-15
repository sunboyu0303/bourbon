package com.github.bourbon.springframework.boot.context.properties.bind.validation;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.springframework.boot.context.properties.bind.*;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationProperty;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.core.ResolvableType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 17:36
 */
public class ValidationBindHandler extends AbstractBindHandler {

    private final Validator[] validators;

    private final Map<ConfigurationPropertyName, ResolvableType> boundTypes = MapUtils.newLinkedHashMap();

    private final Map<ConfigurationPropertyName, Object> boundResults = MapUtils.newLinkedHashMap();

    private final Set<ConfigurationProperty> boundProperties = SetUtils.newLinkedHashSet();

    private BindValidationException exception;

    public ValidationBindHandler(Validator... validators) {
        this.validators = validators;
    }

    public ValidationBindHandler(BindHandler parent, Validator... validators) {
        super(parent);
        this.validators = validators;
    }

    @Override
    public <T> Bindable<T> onStart(ConfigurationPropertyName name, Bindable<T> target, BindContext context) {
        boundTypes.put(name, target.getType());
        return super.onStart(name, target, context);
    }

    @Override
    public Object onSuccess(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        boundResults.put(name, result);
        ConfigurationProperty configurationProperty = context.getConfigurationProperty();
        if (configurationProperty != null) {
            boundProperties.add(configurationProperty);
        }
        return super.onSuccess(name, target, context, result);
    }

    @Override
    public Object onFailure(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Exception error) throws Exception {
        Object result = super.onFailure(name, target, context, error);
        if (result != null) {
            clear();
            boundResults.put(name, result);
        }
        validate(name, target, context, result);
        return result;
    }

    private void clear() {
        this.boundTypes.clear();
        this.boundResults.clear();
        this.boundProperties.clear();
        this.exception = null;
    }

    @Override
    public void onFinish(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) throws Exception {
        validate(name, target, context, result);
        super.onFinish(name, target, context, result);
    }

    private void validate(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        if (exception == null) {
            Object validationTarget = getValidationTarget(target, context, result);
            if (validationTarget != null) {
                validateAndPush(name, validationTarget, target.getBoxedType().resolve());
            }
        }
        if (context.getDepth() == 0 && exception != null) {
            throw exception;
        }
    }

    private Object getValidationTarget(Bindable<?> target, BindContext context, Object result) {
        return ObjectUtils.defaultSupplierIfNull(result, () -> {
            if (context.getDepth() == 0) {
                Supplier<?> value = target.getValue();
                if (value != null) {
                    return value.get();
                }
            }
            return null;
        });
    }

    private void validateAndPush(ConfigurationPropertyName name, Object target, Class<?> type) {
        ValidationResult result = null;
        for (Validator validator : validators) {
            if (validator.supports(type)) {
                result = ObjectUtils.defaultSupplierIfNull(result, () -> new ValidationResult(name, target));
                validator.validate(target, result);
            }
        }
        if (result != null && result.hasErrors()) {
            exception = new BindValidationException(result.getValidationErrors());
        }
    }

    private class ValidationResult extends BeanPropertyBindingResult {

        private static final long serialVersionUID = 2809357494264258310L;

        private final transient ConfigurationPropertyName name;

        private final transient Object target;

        private ValidationResult(ConfigurationPropertyName name, Object target) {
            super(target, null);
            this.name = name;
            this.target = target;
        }

        @Override
        public String getObjectName() {
            return this.name.toString();
        }

        @Override
        public Class<?> getFieldType(String field) {
            return ObjectUtils.defaultSupplierIfNull(ObjectUtils.defaultIfNull(getBoundField(ValidationBindHandler.this.boundTypes, field), ResolvableType::resolve), () -> super.getFieldType(field));
        }

        @Override
        protected Object getActualFieldValue(String field) {
            return ObjectUtils.defaultSupplierIfNull(getBoundField(ValidationBindHandler.this.boundResults, field), () -> {
                try {
                    return super.getActualFieldValue(field);
                } catch (Exception ex) {
                    if (isPropertyNotReadable(ex)) {
                        return null;
                    }
                    throw ex;
                }
            });
        }

        private boolean isPropertyNotReadable(Throwable ex) {
            while (ex != null) {
                if (ex instanceof NotReadablePropertyException) {
                    return true;
                }
                ex = ex.getCause();
            }
            return false;
        }

        private <T> T getBoundField(Map<ConfigurationPropertyName, T> boundFields, String field) {
            try {
                ConfigurationPropertyName name = getName(field);
                T bound = boundFields.get(name);
                if (bound != null) {
                    return bound;
                }
                if (name.hasIndexedElement()) {
                    for (Map.Entry<ConfigurationPropertyName, T> entry : boundFields.entrySet()) {
                        if (isFieldNameMatch(entry.getKey(), name)) {
                            return entry.getValue();
                        }
                    }
                }
            } catch (Exception e) {
                // ignore
            }
            return null;
        }

        private boolean isFieldNameMatch(ConfigurationPropertyName name, ConfigurationPropertyName fieldName) {
            return BooleanUtils.defaultIfFalse(name.getNumberOfElements() == fieldName.getNumberOfElements(), () -> {
                for (int i = 0; i < name.getNumberOfElements(); i++) {
                    if (!ObjectUtils.nullSafeEquals(name.getElement(i, ConfigurationPropertyName.Form.ORIGINAL), fieldName.getElement(i, ConfigurationPropertyName.Form.ORIGINAL))) {
                        return false;
                    }
                }
                return true;
            }, false);
        }

        private ConfigurationPropertyName getName(String field) {
            return name.append(DataObjectPropertyName.toDashedForm(field));
        }

        ValidationErrors getValidationErrors() {
            return new ValidationErrors(name, ValidationBindHandler.this.boundProperties.stream().filter(property -> name.isAncestorOf(property.getName())).collect(Collectors.toCollection(LinkedHashSet::new)), getAllErrors());
        }
    }
}