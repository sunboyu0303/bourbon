package com.github.bourbon.springframework.boot.context.properties.bind.validation;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationProperty;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import com.github.bourbon.springframework.boot.origin.Origin;
import com.github.bourbon.springframework.boot.origin.OriginProvider;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 17:45
 */
public class ValidationErrors implements Iterable<ObjectError> {

    private final ConfigurationPropertyName name;

    private final Set<ConfigurationProperty> boundProperties;

    private final List<ObjectError> errors;

    ValidationErrors(ConfigurationPropertyName name, Set<ConfigurationProperty> boundProperties, List<ObjectError> errors) {
        this.name = ObjectUtils.requireNonNull(name, "Name must not be null");
        this.boundProperties = SetUtils.unmodifiableSet(ObjectUtils.requireNonNull(boundProperties, "BoundProperties must not be null"));
        this.errors = convertErrors(name, boundProperties, ObjectUtils.requireNonNull(errors, "Errors must not be null"));
    }

    private List<ObjectError> convertErrors(ConfigurationPropertyName name, Set<ConfigurationProperty> boundProperties, List<ObjectError> errors) {
        return ListUtils.unmodifiableList(errors.stream().map(e -> convertError(name, boundProperties, e)).collect(Collectors.toList()));
    }

    private ObjectError convertError(ConfigurationPropertyName name, Set<ConfigurationProperty> boundProperties, ObjectError error) {
        return BooleanUtils.defaultIfAssignableFrom(error, FieldError.class, e -> convertFieldError(name, boundProperties, (FieldError) e), error);
    }

    private FieldError convertFieldError(ConfigurationPropertyName name, Set<ConfigurationProperty> boundProperties, FieldError error) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(error, OriginProvider.class, t -> t, () -> OriginTrackedFieldError.of(error, findFieldErrorOrigin(name, boundProperties, error)));
    }

    private Origin findFieldErrorOrigin(ConfigurationPropertyName name, Set<ConfigurationProperty> boundProperties, FieldError error) {
        for (ConfigurationProperty boundProperty : boundProperties) {
            if (isForError(name, boundProperty.getName(), error)) {
                return Origin.from(boundProperty);
            }
        }
        return null;
    }

    private boolean isForError(ConfigurationPropertyName name, ConfigurationPropertyName boundPropertyName, FieldError error) {
        return name.isParentOf(boundPropertyName) && boundPropertyName.getLastElement(ConfigurationPropertyName.Form.UNIFORM).equalsIgnoreCase(error.getField());
    }

    public ConfigurationPropertyName getName() {
        return name;
    }

    public Set<ConfigurationProperty> getBoundProperties() {
        return boundProperties;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ObjectError> getAllErrors() {
        return errors;
    }

    @Override
    public Iterator<ObjectError> iterator() {
        return errors.iterator();
    }
}