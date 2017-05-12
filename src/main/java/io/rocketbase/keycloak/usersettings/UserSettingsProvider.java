package io.rocketbase.keycloak.usersettings;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resource.RealmResourceProvider;

/**
 * Created by mirco on 08.05.17.
 */
public class UserSettingsProvider implements RealmResourceProvider {

    private final KeycloakSession session;

    public UserSettingsProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        AuthenticationManager.AuthResult auth = new AppAuthManager().authenticateBearerToken(session,
                session.getContext()
                        .getRealm());
        return new UserSettingsResource(session.users(),
                auth,
                session.getContext()
                        .getRealm());
    }

    @Override
    public void close() {

    }
}
