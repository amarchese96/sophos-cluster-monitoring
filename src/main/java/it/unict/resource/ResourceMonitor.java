package it.unict.resource;

import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.fabric8.kubernetes.api.model.Node;
import it.unict.telemetry.TelemetryService;

@ApplicationScoped
public class ResourceMonitor {

    @RestClient
    TelemetryService telemetryService;

    public void updateAvailableResources(List<Node> nodeList) {
        Map<String,Float> availableCpuPercentageValues = telemetryService.getNodesAvailableCpu().await().indefinitely();
        Map<String,Float> availableMemoryValues = telemetryService.getNodesAvailableMemory().await().indefinitely();

        nodeList.forEach(node -> {
            String nodeName = node.getMetadata().getName();

            if (availableCpuPercentageValues.containsKey(nodeName)) {
                float availableCpuPercentage = availableCpuPercentageValues.get(nodeName);
                float allocatableCpu = Float.parseFloat(node.getStatus().getAllocatable().get("cpu").getAmount());
                
                node.getMetadata().getLabels().put(
                    "available-cpu",
                    String.valueOf(availableCpuPercentage * allocatableCpu * 1000)
                );
            }

            if (availableMemoryValues.containsKey(nodeName)) {
                node.getMetadata().getLabels().put(
                    "available-memory",
                    String.valueOf(availableMemoryValues.get(nodeName))
                );
            }
        });
    }
    
}
