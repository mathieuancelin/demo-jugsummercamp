package org.jugsummercamp.demo.app;

import org.osgi.util.tracker.ServiceTracker;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.jboss.weld.environment.osgi.api.events.BundleContainerEvents.*;
import org.jboss.weld.environment.osgi.api.events.Invalid;
import org.jboss.weld.environment.osgi.api.events.Valid;
import org.jugsummercamp.demo.fwk.HttpServiceTracker;

@ApplicationScoped
public class App {

    private static final String CONTEXT_ROOT = "/app";

    @Inject @Any Instance<Object> instances;
        
    private ServiceTracker tracker;
    private AtomicBoolean valid = new AtomicBoolean(false);

    public void start(@Observes BundleContainerInitialized init) throws Exception {
        this.tracker = new HttpServiceTracker(
                init.getBundleContext(), 
                getClass().getClassLoader(), 
                instances, CONTEXT_ROOT);
        this.tracker.open();
    }
    
    public void validate(@Observes Valid event) {
        valid.compareAndSet(false, true);
    }

    public void invalidate(@Observes Invalid event) {
        valid.compareAndSet(true, false);
    }

    public boolean isValid() {
        return valid.get();
    }
}
