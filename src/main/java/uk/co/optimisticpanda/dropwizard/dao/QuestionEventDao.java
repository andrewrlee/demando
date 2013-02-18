package uk.co.optimisticpanda.dropwizard.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.EventMapper;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.domain.QuestionType;
import uk.co.optimisticpanda.dropwizard.event.Event;
import uk.co.optimisticpanda.dropwizard.util.CustomJdbiBinders.BindEnumName;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

@RegisterMapper(EventMapper.class)
public interface QuestionEventDao {

	@SqlUpdate("insert into question_event(event_type, payload, createdDate, uuid) values (:change, :content, :createdDate, :uuid)")
	void insert(@BindEnumName("change") Change change, @BindBean Event<Question<?>, Change> event);

	// @SqlUpdate("update question set question_type = :questionType, question_text = : json where id = :id")
	// void update(@BindEnumName("questionType") QuestionType type,
	// @BindAsJson("json") @BindBean Question<?> question);

	@SqlQuery("select * from question_event")
	List<Event<Question<?>, Change>> getAll();

	@SqlQuery("select * from question_event where uuid = :uuid")
	Event<Question<?>, Change> get(@Bind("uuid") String uuid);

	@SqlUpdate("delete from question_event where uuid = :uuid")
	void delete(@Bind("uuid") String id);

//	@SqlQuery("select * from question_event where createdDate < '2013-01-21' and createdDate > '2013-01-20'")
//	Event<Question<?>, Change> get(@Bind("uuid") String uuid);
	
	public enum Change {
		CREATE, UPDATE, DELETE
	}

	public static class EventMapper implements ResultSetMapper<Event<Question<?>, Change>> {
		private final ObjectMapper mapper = new ObjectMapper();
		private final QuestionDao.QuestionMapper questionMapper = new QuestionDao.QuestionMapper();

		public Event<Question<?>, Change> map(int arg0, ResultSet resultSet, StatementContext arg2) throws SQLException {
			long id = resultSet.getLong("id");
			UUID uuid = UUID.fromString(resultSet.getString("uuid"));
			Change change = Change.valueOf(resultSet.getString("event_type"));

			try {
				JsonNode node = mapper.readTree(resultSet.getString("payload"));
				QuestionType type = QuestionType.valueOf(node.get("type").asText());
				Long questionId = node.get("id").asLong();
				Question<?> payload = questionMapper.createQuestion(type, resultSet.getString("payload"));
				payload.setId(questionId);
				return new Event<Question<?>, Change>(id, uuid, change, payload, resultSet.getDate("createdDate"));
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		}

	}

}
