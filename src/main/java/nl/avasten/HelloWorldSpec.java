package nl.avasten;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HelloWorldSpec {
    private String name;
    private String image;
    private String data;
    private int replicas;
    private Map<String, String> env;
}
