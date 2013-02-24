package uk.co.optimisticpanda.dropwizard.domain.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import uk.co.optimisticpanda.dropwizard.domain.BooleanQuestion;
import uk.co.optimisticpanda.dropwizard.domain.FreeTextQuestion;
import uk.co.optimisticpanda.dropwizard.domain.FreeTextQuestion.InputType;
import uk.co.optimisticpanda.dropwizard.domain.Question;

import com.google.common.base.Charsets;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class QuestionSupplier implements LineProcessor<List<Question<?>>>, Supplier<List<Question<?>>> {
	private final AtomicLong id = new AtomicLong();
	private final List<Question<?>> questions = Lists.newArrayList();

	public List<Question<?>> get() {
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
		if (trimmedLine.startsWith("-")) {
			String question = trimmedLine.substring(1).trim();
			questions.add(new BooleanQuestion(id.getAndIncrement(), question));
		} else {
			questions.add(new FreeTextQuestion(id.getAndIncrement(), InputType.FIELD, trimmedLine));
		}
		return true;
	}

	public List<Question<?>> getResult() {
		return questions;
	}


}
