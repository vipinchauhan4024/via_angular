package com.patternservices.rest.serializers;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.patternservices.datatype.ApplicationType;


public class PatternTypeEnumSerializer extends JsonSerializer<ApplicationType> {

	@Override
	public void serialize(ApplicationType value, JsonGenerator generator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		generator.writeStartObject();
		generator.writeFieldName("name");
		generator.writeString(value.getAppName());
		generator.writeFieldName("id");
        generator.writeString(value.getAppId());
		
		
		
		generator.writeEndObject();
	}
}
