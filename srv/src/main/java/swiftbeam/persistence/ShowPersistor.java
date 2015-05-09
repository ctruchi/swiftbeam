package swiftbeam.persistence;

import restx.factory.Component;
import restx.factory.Factory;
import swiftbeam.domain.Show;

@Component
public class ShowPersistor extends Persistor<Show> {
    public ShowPersistor(Factory factory) {
        super(Show.class, factory);
    }
}
