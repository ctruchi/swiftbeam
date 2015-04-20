package swiftbeam.domain;

import swiftbeam.utils.ClassCollections;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

import java.util.Date;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Entity {

    @Id
    private ObjectId id = new ObjectId();

    private Date creationDate;

    private Date modificationDate;

    private Date deletionDate;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @JsonIgnore
    public String getCollectionName() {
        return ClassCollections.getCollectionName(getClass());
    }

    @JsonIgnore
    public <T extends Entity> EntityUri getUri() {
        return new EntityUri(this);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Date getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(Date deletionDate) {
        this.deletionDate = deletionDate;
    }
}
