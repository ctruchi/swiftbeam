package swiftbeam.persistence;

import swiftbeam.domain.Entity;
import restx.factory.Component;
import restx.factory.Factory;

@Component
public class EntityPersistor extends Persistor<Entity> {

    public EntityPersistor(Factory factory) {
        super(Entity.class, factory);
    }
}
