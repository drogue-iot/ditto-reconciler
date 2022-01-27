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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.drogue.cloud.ditto.reconciler.command.CommandClient;
import io.drogue.cloud.ditto.reconciler.model.Event;
import io.drogue.cloud.ditto.reconciler.model.Feature;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class Reconciler {

    private static final Logger log = LoggerFactory.getLogger(Reconciler.class);

    @Inject
    @RestClient
    CommandClient command;

    @Incoming("events")
    public CompletionStage<Void> event(final Event event) {
        log.debug("Event: {}", event);

        final List<CompletableFuture<?>> result = new LinkedList<>();
        for (final Map.Entry<String, Feature> entry : event.getFeatures().entrySet()) {
            result.add(processFeature(event.getThingId(), entry.getKey(), entry.getValue()));
        }

        return CompletableFuture.allOf(result.toArray(CompletableFuture[]::new));
    }

    public CompletableFuture<Void> processFeature(final String thingId, final String name, final Feature feature) {

        final String[] toks = thingId.split(":", 2);
        if (toks.length != 2) {
            return CompletableFuture.completedFuture(null);
        }

        final String application = toks[0];
        final String device = toks[1];

        final Map<String, Object> update = new HashMap<>();

        for (final Map.Entry<String, Object> entry : feature.getDesiredProperties().entrySet()) {
            final Object value = feature.getProperties().get(entry.getKey());
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
    public boolean needUpdate(final Object expected, final Object actual) {
        return !Objects.deepEquals(expected, actual);
    }

    private CompletableFuture<Void> update(final String application, final String device, final String featureName, final Map<String, Object> properties) {
        log.debug("Need to update: {}/{}/{} = {}", application, device, featureName, properties);
        return this.command.sendCommand(application, device, featureName, JsonObject.mapFrom(properties))
                .toCompletableFuture()
                .thenApply((x) -> null);
    }
}
