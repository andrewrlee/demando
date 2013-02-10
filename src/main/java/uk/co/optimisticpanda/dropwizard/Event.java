package uk.co.optimisticpanda.dropwizard;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

public class Event<D> {
	private static final ObjectMapper mapper = new ObjectMapper();

	public enum Type{ CREATE, DELETE, UPDATE}
	
	private final Type type;
	private final D payload;
	private final String uuid;
	private final Date updateDate;
	
	public Event(Type type, D payload){
		this.uuid = UUID.randomUUID().toString();
		this.updateDate = new Date();
		this.payload = payload;
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public D getPayload() {
		return payload;
	}

	public String getUuid() {
		return uuid;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public String getContent() {
		try {
		StringWriter writer = new StringWriter();
			mapper.writeValue(writer, payload);
			return writer.toString();
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
}
