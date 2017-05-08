package io.rocketbase.keycloak.usersettings;

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

/**
 * Created by mirco on 05.05.17.
 */
public class UserSettingsProviderFactory implements RealmResourceProviderFactory {

    // this will be the path: keycloakurl:port/auth/realms/your-realm/profile
    public static final String ID = "profile";

    public RealmResourceProvider create(KeycloakSession session) {
        return new UserSettingsProvider(session);
    }

    public void postInit(KeycloakSessionFactory factory) {

    }

    public void close() {

    }

    public String getId() {
        return ID;
    }

    public void init(Config.Scope config) {

    }
}
