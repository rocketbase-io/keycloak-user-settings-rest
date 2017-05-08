package io.rocketbase.keycloak.usersettings;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

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
    @Produces(MediaType.APPLICATION_JSON)
    public UserWrapper get(@PathParam("id") String id) {
        return new UserWrapper(getUser(id));
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response put(@PathParam("id") String id, @FormParam("preferred_username") String userName, @FormParam("given_name") String firstName, @FormParam("family_name") String lastname,
                        @FormParam("email") String email, @FormParam("avatar") String avatar) {
        UserModel user = getUser(id);
        user.setFirstName(firstName);
        user.setUsername(userName);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setAttribute("avatar", Collections.singletonList(avatar));
        return Response.ok()
                .build();
    }


    private UserModel getUser(String id) {
        return session.users()
                .getUserById(id,
                        session.getContext()
                                .getRealm());
    }


}
