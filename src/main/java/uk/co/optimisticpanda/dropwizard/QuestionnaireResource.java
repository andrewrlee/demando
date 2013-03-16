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

import uk.co.optimisticpanda.dropwizard.domain.Entry;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.domain.Questionnaire;
import uk.co.optimisticpanda.dropwizard.domain.builder.QuestionSupplier;
import uk.co.optimisticpanda.dropwizard.views.QuestionnaireView;

import com.google.common.base.Preconditions;
import com.yammer.metrics.annotation.Timed;


@Path("/questionnaire")
@Produces({MediaType.TEXT_HTML})
public class QuestionnaireResource {

    @GET
    @Timed
    @Path("/{id}")
    public QuestionnaireView getQuestionnaire(@PathParam("id") long id) {
        List<Entry> questions = new QuestionSupplier().get();
        return new QuestionnaireView(new Questionnaire(questions));
    }
    
    @GET
    @Timed
    @Path("/")
    public Collection<Question<?>> getQuestions() {
        return null;
//    	return dao.getAll();
    }
    
    @POST
    @Timed
    @Path("/{id}")
    public void updateQuestion(@PathParam("id") long id, Question<?> question) {
    	Preconditions.checkArgument(id == question.getId(), "Trying to update wrong environment.");
    }
    
    @POST
    @Timed
    @Path("/")
    public void addQuestion(Question<?> question) {
    }
    
    @DELETE
    @Timed
    @Path("/{id}")
    public void deleteQuestion(@PathParam("id") long id) {
    }
    
}
