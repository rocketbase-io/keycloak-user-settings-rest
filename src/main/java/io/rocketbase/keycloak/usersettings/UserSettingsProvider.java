package io.rocketbase.keycloak.usersettings;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
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
        return new UserSettingsResource(session, new AppAuthManager());
    }

    @Override
    public void close() {

    }
}
