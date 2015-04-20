package swiftbeam.serialization;

import swiftbeam.domain.Entity;
import swiftbeam.domain.Reference;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

public class SimpleReferenceSerializer<T extends Entity> extends JsonSerializer<Reference<T>> {

    @Override
    public void serialize(Reference<T> reference, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        provider.defaultSerializeValue(reference.getUri().toString(), jgen);
    }

    @Override
    public void serializeWithType(Reference<T> reference, JsonGenerator jgen, SerializerProvider provider,
                                  TypeSerializer typeSer) throws IOException {
        serialize(reference, jgen, provider);
    }
}
