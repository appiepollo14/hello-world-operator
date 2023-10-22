package nl.avasten;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;

import java.util.Map;

import static io.javaoperatorsdk.operator.api.reconciler.Constants.WATCH_CURRENT_NAMESPACE;

@ControllerConfiguration(namespaces = WATCH_CURRENT_NAMESPACE, name = "helloworld", dependents = {
        @Dependent(type = ServiceDependent.class),
        @Dependent(type = DeploymentDependent.class)
})
public class HelloWorldReconciler implements Reconciler<HelloWorld>, ContextInitializer<HelloWorld> {

    static final String APP_LABEL = "app.kubernetes.io/name";
    static final String LABELS_CONTEXT_KEY = "labels";

    @Override
    public UpdateControl<HelloWorld> reconcile(HelloWorld helloWorld, Context<HelloWorld> context) throws Exception {
        return null;
    }

    @Override
    public void initContext(HelloWorld helloWorld, Context<HelloWorld> context) {
        final var labels = Map.of(APP_LABEL, helloWorld.getMetadata().getName());
        context.managedDependentResourceContext().put(LABELS_CONTEXT_KEY, labels);
    }

    static ObjectMeta createMetadata(HelloWorld resource, Map<String, String> labels) {
        final var metadata = resource.getMetadata();
        return new ObjectMetaBuilder()
                .withName(metadata.getName())
                .withNamespace(metadata.getNamespace())
                .withLabels(labels)
                .build();
    }
}
