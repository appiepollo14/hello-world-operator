package nl.avasten;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class HelloWorldStatus {
    private String host;
    private String message;

    private long waitTime = System.currentTimeMillis();
    private boolean ready = false;

    public HelloWorldStatus() {
        this.message = "processing";
    }

    public HelloWorldStatus(String hostname, String endpoint) {
        this.message = "exposed";
        this.host = endpoint != null && !endpoint.isBlank() ? hostname + '/' + endpoint : hostname;
        this.ready = true;
        this.waitTime = System.currentTimeMillis() - this.getWaitTime();
    }
}
