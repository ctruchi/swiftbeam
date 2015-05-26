package swiftbeam.persistence;

import org.bson.types.ObjectId;
import org.jongo.Aggregate;
import org.jongo.MongoCollection;
import restx.factory.Factory;
import restx.factory.Name;
import restx.jongo.JongoCollection;
import swiftbeam.domain.Entity;
import swiftbeam.domain.EntityUri;
import swiftbeam.utils.ClassCollections;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class Persistor<E extends Entity> {

    private Class<E> clazz;
    private Factory factory;

    public Persistor(Class<E> clazz, Factory factory) {
        this.clazz = clazz;
        this.factory = factory;
    }

    public Optional<E> findOne(String query, Object... parameters) {
        Iterator<E> result = find(query, parameters).iterator();
        return result.hasNext() ? Optional.ofNullable(result.next()) : Optional.<E>empty();
    }

    public Iterable<E> find(String query, Object... parameters) {
        return find(query, Optional.<String>empty(), parameters);
    }

    public Iterable<E> find(String query, String projection, Object... parameters) {
        return find(query, Optional.of(projection), parameters);
    }

    private Iterable<E> find(String query, Optional<String> projection, Object... parameters) {
        Aggregate aggregate = getCollection(clazz).get()
                                                  .aggregate("{$match: " + query + "}", parameters)
                                                  .and("{$redact: {$cond: {" +
                                                               "if: {$gt: ['$deletionDate', null]}, " +
                                                               "then: '$$PRUNE', " +
                                                               "else: '$$KEEP'}}}");

        if (projection.isPresent()) {
            aggregate.and("{$project: " + projection.get() + "}");
        }

        return aggregate.as(clazz);
    }

    public Iterable<E> findWithoutRestrictions(String query, Object... paramaters) {
        return getCollection(clazz).get().find(query, paramaters).as(clazz);
    }

    /**
     * Warning: This does not filter over deletionDate.
     *
     * @param uri
     * @return
     */
    public E findByUri(EntityUri uri) {
        JongoCollection collection = getCollection(uri.getCollectionName());
        return collection.get().findOne("{_id: #}", uri.getId()).as(clazz);
    }

    public Optional<E> findById(ObjectId id) {
        return findOne("{_id: #}", id);
    }

    private JongoCollection getCollection(String collectionName) {
        return factory.queryByName(Name.of(JongoCollection.class, collectionName)).findOneAsComponent().get();
    }

    private JongoCollection getCollection(Class<? extends Entity> clazz) {
        return getCollection(ClassCollections.getCollectionName(clazz));
    }

    public void persist(E entity) {
        if (entity.getCreationDate() == null) {
            entity.setCreationDate(new Date());
        }
        entity.setModificationDate(new Date());
        getCollection(entity.getCollectionName()).get().save(entity);
    }

    public void persistAll(Stream<E> showStream) {
        showStream.forEach(this::persist);
    }

    public void delete(EntityUri uri) {
        MongoCollection collection = getCollection(uri.getCollectionName()).get();
        collection.findAndModify("{id: #}", uri.getId())
                  .with("{$set: {deletionDate: #}}", new Date());
    }

    public void update(ObjectId objectId, String update, Object... parameters) {
        getCollection(clazz).get().update(objectId).with(update, parameters);
    }
}
