package it.unict.telemetry;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Map;

@RegisterRestClient(configKey = "telemetry")
public interface TelemetryService {

    @GET
    @Path("/metrics/nodes/latencies")
    Uni<Map<String,Float>> getNodeLatencies(@QueryParam("node") String node);

    @GET
    @Path("/metrics/nodes/latencies")
    Uni<Map<String,Map<String,Float>>> getNodesLatencies();

    @GET
    @Path("/metrics/nodes/available-cpu")
    Uni<Float> getNodeAvailableCpu(@QueryParam("node") String node);

    @GET
    @Path("/metrics/nodes/available-cpu")
    Uni<Map<String,Float>> getNodesAvailableCpu();

    @GET
    @Path("/metrics/nodes/available-memory")
    Uni<Float> getNodeAvailableMemory(@QueryParam("node") String node);

    @GET
    @Path("/metrics/nodes/available-memory")
    Uni<Map<String,Float>> getNodesAvailableMemory();
}
