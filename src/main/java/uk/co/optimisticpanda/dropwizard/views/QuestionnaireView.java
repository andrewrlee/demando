package uk.co.optimisticpanda.dropwizard.views;

import uk.co.optimisticpanda.dropwizard.domain.Questionnaire;

import com.yammer.dropwizard.views.View;

public class QuestionnaireView extends View{

    private final Questionnaire questionnaire;

    public QuestionnaireView(Questionnaire questionnaire) {
        super("questionnaire.mstch");
        this.questionnaire = questionnaire;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }
    
}
