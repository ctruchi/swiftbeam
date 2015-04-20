package swiftbeam.serialization;

import swiftbeam.domain.Entity;
import swiftbeam.domain.Reference;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

public class ReferenceResolverSerializer<T extends Entity> extends JsonSerializer<Reference<T>> {

    @Override
    public void serialize(Reference<T> value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        //Nothing to do.
    }

    @Override
    public void serializeWithType(Reference<T> value, JsonGenerator jgen, SerializerProvider provider,
                                  TypeSerializer typeSer) throws IOException {
        provider.defaultSerializeValue(value.get(), jgen);
    }
}
