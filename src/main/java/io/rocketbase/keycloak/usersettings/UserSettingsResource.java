package io.rocketbase.keycloak.usersettings;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Created by mirco on 05.05.17.
 */
public class UserSettingsResource {
    private final Logger logger = Logger.getLogger("UserSettingsResource");

    private final KeycloakSession session;


    private final AuthenticationManager.AuthResult auth;

    public UserSettingsResource(KeycloakSession session) {
        this.session = session;
        this.auth = new AppAuthManager().authenticateBearerToken(session,
                session.getContext()
                        .getRealm());
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserWrapper get(@PathParam("id") String id) {
        checkAuth(auth, id);
        return new UserWrapper(getUser(id));
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response put(@PathParam("id") String id, @FormParam("preferred_username") String userName, @FormParam("given_name") String firstName, @FormParam("family_name") String lastname,
                        @FormParam("email") String email, @FormParam("avatar") String avatar) {
        checkAuth(auth, id);
        UserModel user = getUser(id);
        user.setFirstName(firstName);
        user.setUsername(userName);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setAttribute("avatar", Collections.singletonList(avatar));
        return Response.ok()
                .build();
    }

    private void checkAuth(AuthenticationManager.AuthResult auth, String requestedId) {
        if (auth == null) {
            throw new NotAuthorizedException("Bearer");
        } else if (!requestedId.equals(auth.getUser()
                .getId())) {
            throw new org.keycloak.services.ForbiddenException("Wrong user");
        }
    }


    private UserModel getUser(String id) {
        return session.users()
                .getUserById(id,
                        session.getContext()
                                .getRealm());
    }


}
