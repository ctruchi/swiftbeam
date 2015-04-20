package swiftbeam.serialization;

import swiftbeam.domain.Entity;
import swiftbeam.domain.EntityUri;
import swiftbeam.domain.Reference;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferenceDeserializer extends JsonDeserializer<Reference<?>> {


    private static final Pattern URI_PATTERN = Pattern.compile("ref://([^/]+)/(.*)");

    @Override
    public Reference<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;//Nothing to do.
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
                                      TypeDeserializer typeDeserializer) throws IOException {
        JsonToken currentToken = p.getCurrentToken();
        if (currentToken == JsonToken.VALUE_STRING) {
            String uri = p.getValueAsString();
            Matcher matcher = URI_PATTERN.matcher(uri);
            if (!matcher.matches()) {
                throw new IllegalStateException(String.format("Unknown reference format: %s", uri));
            }

            return new Reference<>(new EntityUri(matcher.group(1), new ObjectId(matcher.group(2))));
        } else if (currentToken == JsonToken.START_OBJECT) {
            return new Reference<>(p.readValueAs(Entity.class));
        } else {
            return super.deserializeWithType(p, ctxt, typeDeserializer);
        }
    }
}
