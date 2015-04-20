package swiftbeam.service;

import swiftbeam.domain.User;
import swiftbeam.persistence.AppUserRepository;
import restx.factory.Component;
import restx.security.CredentialsStrategy;
import restx.security.StdUserService;

import javax.inject.Named;
import java.util.Optional;

@Component
public class AppUserService extends StdUserService<User> {

    private AppUserRepository userRepository;

    public AppUserService(AppUserRepository userRepository, CredentialsStrategy credentialsStrategy,
                          @Named("restx.admin.passwordHash") String defaultAdminPasswordHash) {
        super(userRepository, credentialsStrategy, defaultAdminPasswordHash);
        this.userRepository = userRepository;
    }

    public User createUser(User user, Optional<String> password) {
        User dbUser = userRepository.createUser(user);

        if (password.isPresent()) {
            userRepository.setCredentials(dbUser.getLogin(), password.get());
        }

        return dbUser;
    }
}
