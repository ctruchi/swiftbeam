package swiftbeam.persistence;

import swiftbeam.domain.EntityUri;
import swiftbeam.domain.User;
import swiftbeam.utils.ClassCollections;
import org.bson.types.ObjectId;
import restx.factory.Component;
import restx.factory.Factory;
import restx.jongo.JongoCollection;
import restx.jongo.JongoUserRepository;
import restx.security.CredentialsStrategy;

import javax.inject.Named;

@Component
public class AppUserRepository extends JongoUserRepository<User> {

    private static final UserRefStrategy<User> USER_REF_STRATEGY = new UserRefStrategy<User>() {
        @Override
        public String getNameProperty() {
            return "login";
        }

        @Override
        public String getUserRef(User user) {
            return user.getLogin();
        }

        @Override
        public Object toId(String userRef) {
            return userRef;
        }
    };

    private final UserPersistor userPersistor;

    public AppUserRepository(@Named("user") JongoCollection users,
                             @Named("credential") JongoCollection credentials,
                             CredentialsStrategy credentialsStrategy,
                             User defaultAdminUser,
                             Factory factory) {
        super(users, credentials, USER_REF_STRATEGY, credentialsStrategy, User.class, defaultAdminUser);
        userPersistor = new UserPersistor(factory);
    }

    @Override
    public User createUser(User user) {
        userPersistor.persist(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        userPersistor.persist(user);
        return user;
    }

    @Override
    public void deleteUser(String userRef) {
        userPersistor.delete(new EntityUri(ClassCollections.getCollectionName(User.class), new ObjectId(userRef)));
    }

    private static class UserPersistor extends Persistor<User> {

        public UserPersistor(Factory factory) {
            super(User.class, factory);
        }
    }
}
