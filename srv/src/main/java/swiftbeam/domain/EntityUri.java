package swiftbeam.domain;

import org.bson.types.ObjectId;

public class EntityUri {

    private String collectionName;

    private ObjectId id;

    public EntityUri(Entity entity) {
        this(entity.getCollectionName(), entity.getId());
    }

    public EntityUri(String collection, ObjectId id) {
        this.collectionName = collection;
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public String toString() {
        return "ref://" + collectionName + "/" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EntityUri entityUri = (EntityUri) o;

        if (!collectionName.equals(entityUri.collectionName)) {
            return false;
        }
        if (!id.equals(entityUri.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = collectionName.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}
