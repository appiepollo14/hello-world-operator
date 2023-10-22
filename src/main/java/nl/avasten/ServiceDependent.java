package nl.avasten;

import io.fabric8.kubernetes.api.model.Service;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;

public class ServiceDependent extends CRUDKubernetesDependentResource<Service, HelloWorld> {

    public ServiceDependent() {
        super(Service.class);
    }

    @Override
    protected Service desired(HelloWorld primary, Context<HelloWorld> context) {
        return super.desired(primary, context);
    }
}
