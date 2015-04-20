package swiftbeam.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import restx.factory.Component;
import restx.jackson.Views;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class ReferenceSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        System.out.println("boo");
        //Nothing to do
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws
            JsonMappingException {
        if (!Views.Private.class.isAssignableFrom(prov.getActiveView())) {
            Optional<ResolvedIn> annotation = Optional.ofNullable(property.getAnnotation(ResolvedIn.class));
            if (annotation.isPresent() && Arrays.asList(annotation.get().value()).contains(prov.getActiveView())) {
                return new ReferenceResolverSerializer<>();
            }
        }
        return new SimpleReferenceSerializer<>();
    }
}
