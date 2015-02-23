package com.openvraas.services.router;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.openvraas.services.AbstractService;
import com.openvraas.undertow.router.RouterApplication;

public class Router extends AbstractService {

    private static final String PROP_ROUTER_PREFIX    = "com.openvraas.router.";

    private static final String PROP_ROUTER_PORT      = PROP_ROUTER_PREFIX+"port";

    private static final String PROP_ROUTER_IOTHREADS = PROP_ROUTER_PREFIX+"iothread";

    private static final String PROP_ROUTER_METRICS   = PROP_ROUTER_PREFIX+"enableMetrics";

    static {
        if (System.getProperty(PROP_ROUTER_PORT)==null) {
            System.setProperty(PROP_ROUTER_PORT, "8080");
        }
        if (System.getProperty(PROP_ROUTER_IOTHREADS)==null) {
            System.setProperty(PROP_ROUTER_IOTHREADS, String.valueOf(Runtime.getRuntime().availableProcessors()));
        }
        if (System.getProperty(PROP_ROUTER_METRICS)==null) {
            System.setProperty(PROP_ROUTER_METRICS, "false");
        }

    }

    @PostConstruct
    protected void init() {

        super.prelaunch();

        int port = Integer.parseInt(System.getProperty(PROP_ROUTER_PORT));
        String iothreads = System.getProperty(PROP_ROUTER_IOTHREADS);

        final Map<String, String> options = new HashMap<>();
        options.put("IoThreads", iothreads);
        options.put("EnableMetrics", !"false".equals(System.getProperty(PROP_ROUTER_METRICS)) ? "true" : "false");

        new RouterApplication().setHost("0.0.0.0")
                               .setPort(port)
                               .setOptions(options)
                               .setFarm(farm)
                               .start();

        onLog("DEBUG", "[0.0.0.0:"+String.valueOf(port)+"] ready");
    }

    public Router() {
        super();
    }

}
