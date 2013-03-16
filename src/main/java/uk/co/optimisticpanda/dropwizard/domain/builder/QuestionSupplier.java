package uk.co.optimisticpanda.dropwizard.domain.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import uk.co.optimisticpanda.dropwizard.domain.Entry;
import uk.co.optimisticpanda.dropwizard.domain.RadioButtonQuestion;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class QuestionSupplier implements LineProcessor<List<Entry>>, Supplier<List<Entry>> {
    private final AtomicLong id = new AtomicLong();
    private final List<Entry> questions = Lists.newArrayList();

    public List<Entry> get() {
        try {
            return Files.readLines(new File("src/main/resources/questions.txt"), Charsets.UTF_8, this);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public boolean processLine(String line) throws IOException {
        String trimmedLine = line.trim();
        if (trimmedLine.startsWith("#")) {
            return true;
        }
        if (trimmedLine.startsWith("%")) {
            int optionsIndex = trimmedLine.indexOf("{");
            String question = trimmedLine.substring(1, optionsIndex).trim();
            RadioButtonQuestion q = new RadioButtonQuestion(id.getAndIncrement(), question);
            for (String option : Splitter.on(',').omitEmptyStrings().trimResults().split(trimmedLine.substring(optionsIndex + 1))) {
                q.addOptions(option);
            }
            questions.add(q);
        } 
//        else if (trimmedLine.startsWith("-")) {
//            String question = trimmedLine.substring(1).trim();
//            questions.add(new BooleanQuestion(id.getAndIncrement(), question));
//        } else {
//            questions.add(new FreeTextQuestion(id.getAndIncrement(), InputType.FIELD, trimmedLine));
//        }
        return true;
    }

    public List<Entry> getResult() {
        return questions;
    }

}
