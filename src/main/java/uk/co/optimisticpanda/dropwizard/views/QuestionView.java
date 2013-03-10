package uk.co.optimisticpanda.dropwizard.views;

import uk.co.optimisticpanda.dropwizard.domain.Question;

import com.yammer.dropwizard.views.View;

public class QuestionView extends View{

    private final Question<?> question;

    public QuestionView(Question<?> question) {
        super("radio_button.mstch");
        this.question = question;
    }

    public Question<?> getQuestion() {
        return question;
    }
    
}
