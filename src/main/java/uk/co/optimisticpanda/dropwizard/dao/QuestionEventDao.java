package uk.co.optimisticpanda.dropwizard.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.QuestionEventMapper;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.domain.QuestionType;
import uk.co.optimisticpanda.dropwizard.event.Event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

@RegisterMapper(QuestionEventMapper.class)
public interface QuestionEventDao {

    @SqlUpdate("insert into question_event(event_type, payload, createdDate, uuid) values (:category, :content, :createdDate, :uuid)")
    void insert(@BindBean Event<Question<?>> event);

    @SqlQuery("select * from question_event where id >= :from and id <= :to order by id desc")
    List<Event<Question<?>>> get(@Bind("from") long from, @Bind("to") long to);

    @RegisterMapper(SingleRowMapMapper.class)
    @SqlQuery("select min(id) as min, max(id) as max from question_event;")
    Map<String, Object> getMinAndMax();

    public static class QuestionEventMapper implements ResultSetMapper<Event<Question<?>>> {
        private final ObjectMapper mapper = new ObjectMapper();
        private final QuestionDao.QuestionMapper questionMapper = new QuestionDao.QuestionMapper();

        public Event<Question<?>> map(int arg0, ResultSet resultSet, StatementContext arg2) throws SQLException {
            long id = resultSet.getLong("id");
            UUID uuid = UUID.fromString(resultSet.getString("uuid"));
            String category = resultSet.getString("event_type");

            try {
                JsonNode node = mapper.readTree(resultSet.getString("payload"));
                QuestionType type = QuestionType.valueOf(node.get("type").asText());
                Long questionId = node.get("id").asLong();
                Question<?> payload = questionMapper.createQuestion(type, resultSet.getString("payload"));
                payload.setId(questionId);
                return new Event<Question<?>>(id, uuid, category, payload, resultSet.getDate("createdDate"));
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }
    }

    public static class SingleRowMapMapper implements ResultSetMapper<Map<String, Object>> {

        public Map<String, Object> map(int arg0, ResultSet resultSet, StatementContext arg2) throws SQLException {
            Preconditions.checkArgument(resultSet.isFirst());
            Map<String, Object> result = Maps.newHashMap();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String name = metaData.getColumnName(i).toLowerCase();
                result.put(name, resultSet.getObject(i));
            }
            return result;
        }
    }

}
