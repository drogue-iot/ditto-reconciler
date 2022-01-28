package io.drogue.cloud.ditto.reconciler.model;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.MoreObjects;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Feature {
    private Map<String, Object> properties = new HashMap<>();
    private Map<String, Object> desiredProperties = new HashMap<>();

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void setProperties(final Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getDesiredProperties() {
        return this.desiredProperties;
    }

    public void setDesiredProperties(final Map<String, Object> desiredProperties) {
        this.desiredProperties = desiredProperties;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("properties", this.properties)
                .add("desiredProperties", this.desiredProperties)
                .toString();
    }
}
