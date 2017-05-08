package io.rocketbase.keycloak.usersettings;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by mirco on 05.05.17.
 */
public class UserSettingsProvider implements RealmResourceProvider {
    private final KeycloakSession session;

    //  private final Gson gson = new Gson();

    //  private final AuthenticationManager.AuthResult auth;

    public UserSettingsProvider(KeycloakSession session) {
        this.session = session;
//        this.auth = new AppAuthManager().authenticateBearerToken(session,
//                session.getContext()
//                        .getRealm());
    }

    public Object getResource() {
        return this;
    }

    public void close() {

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String get(@PathParam("id") String id) {
        UserModel user = session.users()
                .getUserById(id,
                        session.getContext()
                                .getRealm());

        return user.getFirstName();
    }

}
