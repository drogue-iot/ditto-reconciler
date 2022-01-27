package io.drogue.cloud.ditto.reconciler.model;

public class EventDeserializer extends io.quarkus.kafka.client.serialization.ObjectMapperDeserializer<Event> {
    public EventDeserializer() {
        super(Event.class);
    }
}
