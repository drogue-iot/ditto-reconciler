package io.drogue.cloud.ditto.reconciler.model;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.MoreObjects;

public class Feature {
    private Map<String, Object> properties = new HashMap<>();
    private Map<String, Object> desiredProperties = new HashMap<>();

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getDesiredProperties() {
        return desiredProperties;
    }

    public void setDesiredProperties(Map<String, Object> desiredProperties) {
        this.desiredProperties = desiredProperties;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("properties", properties)
                .add("desiredProperties", desiredProperties)
                .toString();
    }
}
