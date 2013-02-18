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
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class QuestionGenerator {
	private AtomicLong id = new AtomicLong();
	
	public static void main(String[] args) throws IOException {
		for (Question<?> question : new QuestionGenerator().gather()) {
			System.out.println(question);
		}
	}
	
	public List<Question<?>> gather() throws IOException{
		return Files.readLines(new File("src/main/resources/questions.txt"), Charsets.UTF_8, new LineProcessor<List<Question<?>>>() {
			private List<Question<?>> questions = Lists.newArrayList();
			public List<Question<?>> getResult() {
				return questions;
			}

			public boolean processLine(String line) throws IOException {
				if(!line.trim().startsWith("#")){
					if(line.trim().startsWith("-")){
						String question = line.trim().substring(1).trim();
						questions.add(new BooleanQuestion(id.getAndIncrement(), question));
					}else{
						questions.add(new FreeTextQuestion(id.getAndIncrement(), InputType.FIELD, line.trim()));
					}
				}
				return true;
			}
		});
	}
	
}
