package uk.co.optimisticpanda.dropwizard;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Preconditions;
import com.yammer.metrics.annotation.Timed;

@Path("/environment")
//@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML})
@Produces({MediaType.APPLICATION_JSON})
public class EnvironmentResource {

    private final EnvironmentStore store;

	public EnvironmentResource(EnvironmentStore store) {
        this.store = store;
    }

    @GET
    @Timed
    @Path("/{id}")
    public Environment getEnvironment(@PathParam("id") long id) {
        return store.getEnvironment(id);
    }
    
    @GET
    @Timed
    @Path("/{id}/children")
    public List<Environment> getChildren(@PathParam("id") long id) {
    	return store.getEnvironment(id).getChildren();
    }
    
    @GET
    @Timed
    @Path("/")
    public Collection<Environment> getEnvironments() {
    	return store.getEnvironments();
    }

    @POST
    @Timed
    @Path("/{id}")
    public void updateEnvironment(@PathParam("id") long id, Environment environment) {
    	Preconditions.checkArgument(id == environment.getId(), "Trying to update wrong environment.");
    	store.updateEnvironment(environment);
    }
    @POST
    @Timed
    @Path("/")
    public void addEnvironment(Environment environment) {
    	store.addEnvironment(environment);
    }
    
    @DELETE
    @Timed
    @Path("/{id}")
    public void deleteEnvironment(@PathParam("id") long id) {
    	store.deleteEnvironment(id);
    }
    
}