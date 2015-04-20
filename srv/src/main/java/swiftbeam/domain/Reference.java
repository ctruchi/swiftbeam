package swiftbeam.domain;

import swiftbeam.persistence.EntityPersistor;
import swiftbeam.serialization.ReferenceDeserializer;
import swiftbeam.serialization.ReferenceSerializer;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSerialize(using = ReferenceSerializer.class)
@JsonDeserialize(using = ReferenceDeserializer.class)
public class Reference<T extends Entity> {

    private static final Logger logger = LoggerFactory.getLogger(Reference.class);

    //TODO Set this in settings or at least AppModule
    private static final int GET_ENTITY_THRESHOLD = 20;

    private static final ThreadLocal<EntityPersistor> PERSISTOR = new ThreadLocal<>();
    private static final ThreadLocal<Integer> GET_ENTITY_COUNTER = new ThreadLocal<>();

    private EntityUri uri;

    private Optional<T> target = Optional.empty();

    public Reference(EntityUri uri) {
        this.uri = uri;
    }

    public Reference(T entity) {
        this.target = Optional.ofNullable(entity);

        if (this.target.isPresent()) {
            this.uri = this.target.get().getUri();
        }
    }

    public static void initPersistor(EntityPersistor persistor) {
        PERSISTOR.set(persistor);
        GET_ENTITY_COUNTER.set(0);
    }

    public static void clearPersistor() {
        if (GET_ENTITY_COUNTER.get() > GET_ENTITY_THRESHOLD) {
            logger.warn("You fetched {} entities from db on one thread", GET_ENTITY_COUNTER.get());
        }

        PERSISTOR.set(null);
        GET_ENTITY_COUNTER.set(null);
    }

    public EntityUri getUri() {
        return uri;
    }

    public T get() {

        if(!target.isPresent()) {
            handleGetEntityCounter();
            target = Optional.of((T) getPersistor().findByUri(uri));
        }

        return target.get();
    }

    private void handleGetEntityCounter() {
        GET_ENTITY_COUNTER.set(GET_ENTITY_COUNTER.get() + 1);

        logger.debug("Nb of entity fetch from db: {}", GET_ENTITY_COUNTER.get());
    }

    private EntityPersistor getPersistor() {
        return PERSISTOR.get();
    }

    @Override
    public String toString() {
        return "Reference{" +
                "uri=" + uri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Reference reference = (Reference) o;

        return uri.equals(reference.uri);

    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
