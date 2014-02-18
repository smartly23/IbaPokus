package cz.fi.muni.pa165.calorycounter.frontend.restserver;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * Alternative root servlet class, which can be used insted of ConfigApplication. See web.xml: 
 * servlet/init-param/param-value"
 * 
 * @author mpasko
 */

public class MyApplication extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public MyApplication() {
        register(RequestContextFilter.class);
        register(ProfileRestResource.class);
    }
}