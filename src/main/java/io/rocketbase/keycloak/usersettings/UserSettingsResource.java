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
    private final static Logger LOGGER = Logger.getLogger("UserSettingsResource");

    private final KeycloakSession session;

    private final AppAuthManager authManager;

    private final boolean changeUserNameAllowed;

    public UserSettingsResource(KeycloakSession session, AppAuthManager authManager, boolean changeUserNameAllowed) {
        this.session = session;
        this.authManager = authManager;
        this.changeUserNameAllowed = changeUserNameAllowed;


    }

    @POST
    @Path("{id}")
    public Response post() {
        return Response.status(405)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response delete() {
        return Response.status(405)
                .build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") String id, UserWrapper wrapper) {
        LOGGER.info(wrapper.toString());
        authorize(authManager, id);
        validate(id, wrapper.getPreferred_username());
        UserModel user = getUserById(id);
        user.setFirstName(wrapper.getGiven_name());
        user.setUsername(wrapper.getPreferred_username());
        user.setLastName(wrapper.getFamily_name());
        user.setEmail(wrapper.getEmail());
        user.setAttribute("avatar", Collections.singletonList(wrapper.getAvatar()));
        return Response.ok()
                .build();
    }

    /**
     * Checks if the user is authorized to perform the request. Responses with 401 if no bearer was provided / 403 if the ID of the logged in user is different from the called ID.
     *
     * @param manager
     * @param requestedId id of the user to change
     */
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

    /**
     * Usernames must be uniqued and are required (KeyCloak constraint). Wraps the default 500 response to a more meaningful 400.
     *
     * @param id       the id
     * @param username the new username
     */
    private void validate(String id, String username) {

        if (username == null || "".equals(username)) {
            throw new ClientErrorException("Username is required", Response.Status.BAD_REQUEST);
        }
        UserModel byId = getUserById(id);
        UserModel byName = getUserByName(username);
        if (byName != null && !byId.getId()
                .equals(byName.getId())) {
            throw new ClientErrorException("Username must be unique", Response.Status.BAD_REQUEST);
        }
        if (!changeUserNameAllowed && !username.equals(byId.getUsername())) {
            throw new ClientErrorException("Username can not be modified", Response.Status.BAD_REQUEST);
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
