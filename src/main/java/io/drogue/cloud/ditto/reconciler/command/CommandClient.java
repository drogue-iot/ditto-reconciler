package io.drogue.cloud.ditto.reconciler.command;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.vertx.core.json.JsonObject;

@RegisterRestClient
@ClientHeaderParam(name = "Authorization", value = "{authorization}")
public interface CommandClient {

    default String authorization() {
        Config config = ConfigProvider.getConfig();
        Optional<String> username = config.getOptionalValue("io.drogue.cloud.command.client.username", String.class);
        Optional<String> password = config.getOptionalValue("io.drogue.cloud.command.client.password", String.class);
        if (username.isPresent() && password.isPresent()) {
            return "Basic " + Base64.getEncoder().encodeToString(String.format("%s:%s", username.get(), password.get()).getBytes(StandardCharsets.UTF_8));
        } else {
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces
    @Path("/api/command/v1alpha1/apps/{application}/devices/{device}")
    CompletionStage<byte[]> sendCommand(
            @PathParam("application")
                    String application,
            @PathParam("device")
                    String device,
            @QueryParam("command")
                    String command,
            JsonObject payload);

}
