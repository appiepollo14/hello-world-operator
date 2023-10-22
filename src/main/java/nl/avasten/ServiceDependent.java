package nl.avasten;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;

import java.util.Map;

import static nl.avasten.HelloWorldReconciler.LABELS_CONTEXT_KEY;
import static nl.avasten.HelloWorldReconciler.createMetadata;

public class ServiceDependent extends CRUDKubernetesDependentResource<Service, HelloWorld> {

    public ServiceDependent() {
        super(Service.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Service desired(HelloWorld helloWorld, Context context) {
        final var labels = (Map<String, String>) context.managedDependentResourceContext()
                .getMandatory(LABELS_CONTEXT_KEY, Map.class);

        return new ServiceBuilder()
                .withMetadata(createMetadata(helloWorld, labels))
                .withNewSpec()
                .addNewPort()
                .withName("http")
                .withPort(8080)
                .withNewTargetPort().withValue(8080).endTargetPort()
                .endPort()
                .withSelector(labels)
                .withType("ClusterIP")
                .endSpec()
                .build();
    }
}
