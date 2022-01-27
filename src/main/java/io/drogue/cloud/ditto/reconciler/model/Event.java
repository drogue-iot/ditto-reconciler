package io.drogue.cloud.ditto.reconciler.model;

import java.util.Map;

public class Event {
    private String thingId;
    private Map<String, Feature> features;

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public String getThingId() {
        return thingId;
    }

    public Map<String, Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("thingId", thingId)
                .add("features", features)
                .toString();
    }
}

