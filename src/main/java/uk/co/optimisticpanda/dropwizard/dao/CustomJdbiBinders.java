package uk.co.optimisticpanda.dropwizard.dao;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

public class CustomJdbiBinders {

	@BindingAnnotation(BindAsJson.JsonBinderFactory.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.PARAMETER })
	public @interface BindAsJson {
		String value();

		public static class JsonBinderFactory implements BinderFactory {
			private final ObjectMapper mapper = new ObjectMapper();
			public Binder<BindAsJson, Object> build(Annotation annotation) {
				return new Binder<BindAsJson, Object>() {
					public void bind(SQLStatement<?> q, BindAsJson arg1, Object o) {
						StringWriter writer = new StringWriter();
						try {
							mapper.writeValue(writer, o);
							q.bind(arg1.value(), writer.toString());
						} catch (IOException e) {
							Throwables.propagate(e);
						}
					}
				};
			}
		}
	}
	
	@BindingAnnotation(BindEnumName.EnumNameBinderFactory.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.PARAMETER })
	public @interface BindEnumName{
		String value();
		public static class EnumNameBinderFactory implements BinderFactory {
			public Binder<BindEnumName, Enum<?>> build(Annotation annotation) {
				return new Binder<BindEnumName, Enum<?>>(){
					public void bind(SQLStatement<?> query, BindEnumName arg1, Enum<?> o) {
						query.bind(arg1.value(), o.name());
						
					}};
			}
		}
	}

}
