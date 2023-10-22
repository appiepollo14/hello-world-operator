package nl.avasten;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("avasten.nl")
@Version("v1alpha1")
@ShortNames("hw")
public class HelloWorld extends CustomResource<HelloWorldSpec, HelloWorldStatus> implements Namespaced {
}
