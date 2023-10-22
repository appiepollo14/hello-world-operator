package nl.avasten;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.Matcher;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.avasten.HelloWorldReconciler.LABELS_CONTEXT_KEY;
import static nl.avasten.HelloWorldReconciler.createMetadata;

public class DeploymentDependent extends CRUDKubernetesDependentResource<Deployment, HelloWorld> implements Matcher<Deployment, HelloWorld> {

    public DeploymentDependent() {
        super(Deployment.class);
    }

    @Override
    protected Deployment desired(HelloWorld helloWorld, Context<HelloWorld> context) {
        final var labels = (Map<String, String>) context.managedDependentResourceContext()
                .getMandatory(LABELS_CONTEXT_KEY, Map.class);
        final var name = helloWorld.getMetadata().getName();
        final var spec = helloWorld.getSpec();
        final var imageRef = spec.getImage();
        final var env = spec.getEnv();

        var containerBuilder = new DeploymentBuilder()
                .withMetadata(createMetadata(helloWorld, labels))
                .withNewSpec()
                .withNewSelector().withMatchLabels(labels).endSelector()
                .withNewTemplate()
                .withNewMetadata().withLabels(labels).endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName(name).withImage(imageRef);

        // add env variables
        if (env != null) {
            env.forEach((key, value) -> containerBuilder.addNewEnv()
                    .withName(key.toUpperCase())
                    .withValue(value)
                    .endEnv());
        }

        return containerBuilder
                .addNewPort()
                .withName("http").withProtocol("TCP").withContainerPort(8080)
                .endPort()
                .endContainer()
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();
    }


    @Override
    public Result<Deployment> match(Deployment actualResource, HelloWorld primary, Context<HelloWorld> context) {
        final var desiredSpec = primary.getSpec();
        final var container = actualResource.getSpec().getTemplate().getSpec().getContainers()
                .stream()
                .findFirst();
        return Result.nonComputed(container.map(c -> c.getImage().equals(desiredSpec.getImage())
                && desiredSpec.getEnv().equals(convert(c.getEnv()))).orElse(false));
    }

    private Map<String, String> convert(List<EnvVar> envVars) {
        final var result = new HashMap<String, String>(envVars.size());
        envVars.forEach(e -> result.put(e.getName(), e.getValue()));
        return result;
    }
}
