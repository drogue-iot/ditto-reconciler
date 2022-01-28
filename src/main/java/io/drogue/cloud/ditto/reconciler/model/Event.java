package io.drogue.cloud.ditto.reconciler.model;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Event {
    private String thingId;
    private Map<String, Feature> features = new HashMap<>();

    public void setThingId(final String thingId) {
        this.thingId = thingId;
    }

    public String getThingId() {
        return this.thingId;
    }

    public Map<String, Feature> getFeatures() {
        return this.features;
    }

    public void setFeatures(final Map<String, Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("thingId", this.thingId)
                .add("features", this.features)
                .toString();
    }
}

