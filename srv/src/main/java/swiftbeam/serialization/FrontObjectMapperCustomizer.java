package swiftbeam.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;
import restx.factory.Component;
import restx.factory.NamedComponent;
import restx.factory.SingleComponentNameCustomizerEngine;
import restx.jackson.FrontObjectMapperFactory;

import java.io.IOException;

@Component
public class FrontObjectMapperCustomizer extends SingleComponentNameCustomizerEngine<ObjectMapper> {

    public FrontObjectMapperCustomizer() {
        super(0, FrontObjectMapperFactory.NAME);
    }

    @Override
    public NamedComponent<ObjectMapper> customize(NamedComponent<ObjectMapper> namedComponent) {
        ObjectMapper objectMapper = namedComponent.getComponent();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new SimpleModule().addSerializer(ObjectId.class, new JsonSerializer<ObjectId>() {
            @Override
            public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                provider.defaultSerializeValue(value.toString(), jgen);
            }
        }));
        return namedComponent;
    }
}
