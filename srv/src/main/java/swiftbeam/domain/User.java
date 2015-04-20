package swiftbeam.domain;

import swiftbeam.annotations.MongoEntity;
import com.google.common.collect.ImmutableSet;
import restx.security.RestxPrincipal;

import java.util.HashSet;
import java.util.Set;

@MongoEntity("user")
public class User<P extends Entity> extends Entity implements RestxPrincipal {

    private String login;
    private Set<String> roles = new HashSet<>();

    private transient String password;

    @Override
    public ImmutableSet<String> getPrincipalRoles() {
        return ImmutableSet.copyOf(roles);
    }

    @Override
    public String getName() {
        return getLogin();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
