package uk.co.optimisticpanda.dropwizard.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import uk.co.optimisticpanda.dropwizard.dao.CustomJdbiBinders.BindAsJson;
import uk.co.optimisticpanda.dropwizard.dao.CustomJdbiBinders.BindEnumName;
import uk.co.optimisticpanda.dropwizard.dao.QuestionDao.QuestionMapper;
import uk.co.optimisticpanda.dropwizard.domain.BooleanQuestion;
import uk.co.optimisticpanda.dropwizard.domain.FreeTextQuestion;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.domain.QuestionType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

@RegisterMapper(QuestionMapper.class)
public interface QuestionDao {

	@SqlUpdate("insert into question(question_type, question_text, serialized_body) values (:questionType, :questionText, :json)")
	void insert(@BindEnumName("questionType") QuestionType type, @BindAsJson("json") @BindBean Question<?> question);

	@SqlUpdate("update question set question_type = :questionType, question_text = :json where id = :id")
	void update(@BindEnumName("questionType") QuestionType type, @BindAsJson("json") @BindBean Question<?> question);

	@SqlQuery("select * from question where question_type = :type")
	List<Question<?>> getAll(@BindEnumName("type") QuestionType type);

	@SqlQuery("select * from question where id = :id")
	Question<?> get(@Bind("id") long id);

	@SqlQuery("select * from question")
	List<Question<?>> getAll();

	@SqlUpdate("delete from question where id = :id")
	void deleteQuestion(@Bind("id") long id);

	public static class QuestionMapper implements ResultSetMapper<Question<?>> {
		public static ObjectMapper mapper = new ObjectMapper();

		public Question<?> map(int arg0, ResultSet resultSet, StatementContext arg2) throws SQLException {
			Question<?> question = readQuestion(resultSet);
			question.setId(resultSet.getLong("id"));
			return question;
		}

		public Question<?> readQuestion(ResultSet resultSet) throws SQLException {
			QuestionType questionType = QuestionType.valueOf(resultSet.getString("question_type"));
			return createQuestion(questionType, resultSet.getString("serialized_body"));
		}

		public Question<?> createQuestion(QuestionType questionType, String jsonRepresentation) {
			try {
				switch (questionType) {
				case BOOLEAN:
					return mapper.readValue(jsonRepresentation, BooleanQuestion.class);
				case FREE_TEXT:
					return mapper.readValue(jsonRepresentation, FreeTextQuestion.class);
				default:
					throw new UnsupportedOperationException("Cannot handle:" + questionType);
				}
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		}
	}

}
