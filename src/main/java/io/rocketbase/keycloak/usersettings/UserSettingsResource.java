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


    private final AppAuthManager authManager;

    public UserSettingsResource(KeycloakSession session, AppAuthManager authManager) {
        this.session = session;
        this.authManager = authManager;

    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserWrapper get(@PathParam("id") String id) {
        authorize(authManager, id);
        return new UserWrapper(getUserById(id));
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response put(@PathParam("id") String id, @FormParam("preferred_username") String userName, @FormParam("given_name") String firstName, @FormParam("family_name") String lastname,
                        @FormParam("email") String email, @FormParam("avatar") String avatar) {
        authorize(authManager, id);
        validate(userName);
        UserModel user = getUserById(id);
        user.setFirstName(firstName);
        user.setUsername(userName);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setAttribute("avatar", Collections.singletonList(avatar));
        return Response.ok()
                .build();
    }


    private void authorize(AppAuthManager manager, String requestedId) {
        AuthenticationManager.AuthResult auth = authManager.authenticateBearerToken(session,
                session.getContext()
                        .getRealm());
        if (auth == null) {
            // will result in 401 response
            throw new NotAuthorizedException("Bearer");
        } else if (!requestedId.equals(auth.getUser()
                .getId())) {
            // will result in 403 response
            throw new org.keycloak.services.ForbiddenException("Wrong user");
        }
    }

    private void validate(String username) {
        if (getUserByName(username) != null) {
            throw new ClientErrorException("Username must be unique", Response.Status.BAD_REQUEST);
        }
    }

    private UserModel getUserById(String id) {
        return session.users()
                .getUserById(id,
                        session.getContext()
                                .getRealm());
    }

    private UserModel getUserByName(String name) {
        return session.users()
                .getUserByUsername(name,
                        session.getContext()
                                .getRealm());
    }


}
