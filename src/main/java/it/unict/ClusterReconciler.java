package it.unict;

import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import it.unict.network.NetworkCostMonitor;
import it.unict.resource.ResourceMonitor;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ClusterReconciler implements Reconciler<Cluster> { 
  private final KubernetesClient client;

  @Inject
  NetworkCostMonitor networkCostMonitor;

  @Inject
  ResourceMonitor resourceMonitor;

  public ClusterReconciler(KubernetesClient client) {
    this.client = client;
  }

  @Override
  public UpdateControl<Cluster> reconcile(Cluster resource, Context context) {
    Map<String, String> nodeSelectorMap = resource.getSpec().getNodeSelector();
    if (nodeSelectorMap == null) {
      nodeSelectorMap = new HashMap<>();
    }

    List<Node> nodeList = client.nodes()
            .withLabelSelector(new LabelSelectorBuilder().withMatchLabels(nodeSelectorMap).build())
            .list()
            .getItems();

    resourceMonitor.updateAvailableResources(nodeList);
    networkCostMonitor.updateNetworkCosts(nodeList);

    nodeList.forEach(node -> {
      client.nodes().withName(node.getMetadata().getName()).patch(node);
    });

    return UpdateControl.<Cluster>noUpdate().rescheduleAfter(resource.getSpec().getRunInterval(), TimeUnit.SECONDS);
  }
}

