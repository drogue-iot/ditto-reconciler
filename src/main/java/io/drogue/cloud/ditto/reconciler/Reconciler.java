package io.drogue.cloud.ditto.reconciler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.drogue.cloud.ditto.reconciler.command.CommandClient;
import io.drogue.cloud.ditto.reconciler.model.Event;
import io.drogue.cloud.ditto.reconciler.model.Feature;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class Reconciler {

    @Inject
    @RestClient
    CommandClient command;

    @Incoming("events")
    public CompletionStage<Void> event(Event event) {
        System.out.println(event);

        List<CompletableFuture<?>> result = new LinkedList<>();
        for (Map.Entry<String, Feature> entry : event.getFeatures().entrySet()) {
            result.add(processFeature(event.getThingId(), entry.getKey(), entry.getValue()));
        }

        return CompletableFuture.allOf(result.toArray(CompletableFuture[]::new));
    }

    public CompletableFuture<Void> processFeature(String thingId, String name, Feature feature) {

        String[] toks = thingId.split(":", 2);
        if (toks.length != 2) {
            return CompletableFuture.completedFuture(null);
        }

        String application = toks[0];
        String device = toks[1];

        final Map<String, Object> update = new HashMap<>();

        for (Map.Entry<String, Object> entry : feature.getDesiredProperties().entrySet()) {
            Object value = feature.getProperties().get(entry.getKey());
            if (needUpdate(entry.getValue(), value)) {
                update.put(entry.getKey(), entry.getValue());
            }
        }

        if (!update.isEmpty()) {
            return update(application, device, name, update);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    /*
     * Check if a property needs to be updated.
     */
    public boolean needUpdate(Object expected, Object actual) {
        return !Objects.deepEquals(expected, actual);
    }

    private CompletableFuture<Void> update(String application, String device, String featureName, Map<String, Object> properties) {
        System.out.format("Need to update: %s/%s/%s = %s%n", application, device, featureName, properties);
        return command.sendCommand(application, device, featureName, JsonObject.mapFrom(properties))
                .toCompletableFuture()
                .thenApply((x) -> null);
    }
}
