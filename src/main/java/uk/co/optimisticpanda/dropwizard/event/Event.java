package uk.co.optimisticpanda.dropwizard.event;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

public class Event<D extends Resource, C extends Enum<C>> {
	private static final ObjectMapper mapper = new ObjectMapper();

	private Long id;
	private final C type;
	private final D payload;
	private final String uuid;
	private final Date createdDate;
	
	public Event(Long id, C type, D payload, Date date){
		this.uuid = UUID.randomUUID().toString();
		this.createdDate = date;
		this.payload = payload;
		this.type = type;
	}

	public Event(Long id, UUID uuid, C type, D payload, Date createdDate) {
		this.id = id;
		this.uuid = uuid.toString();
		this.type = type;
		this.createdDate = createdDate;
		this.payload= payload;
	}

	public C getEventType() {
		return type;
	}
	
	public Long getId() {
		return id;
	}

	public D getPayload() {
		return payload;
	}

	public String getUuid() {
		return uuid;
	}

	public Date getCreatedDate() {
		return createdDate;
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
