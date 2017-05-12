package io.rocketbase.keycloak.usersettings;

import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
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


    private final AuthenticationManager.AuthResult auth;

    private final UserProvider userProvider;

    private final RealmModel realm;

    private final Validator validator;

    public UserSettingsResource(UserProvider userProvider, AuthenticationManager.AuthResult authManager, RealmModel realm) {
        this.userProvider = userProvider;
        this.auth = authManager;
        this.realm = realm;
        validator = new Validator(
                userProvider, realm);


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
        authorize(id);
        return new UserWrapper(userProvider
                .getUserById(id,
                        realm));
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") String id, UserWrapper wrapper) {
        LOGGER.info(wrapper.toString());
        wrapper.setId(id);
        authorize(id);
        validator.validate(wrapper);
        updateUser(wrapper);

        return Response.ok()
                .build();
    }

    /**
     * Checks if the user is authorized to perform the request. Responses with 401 if no bearer was provided / 403 if the ID of the logged in user is different from the called ID.
     *
     * @param requestedId id of the user to change
     */
    private void authorize(String requestedId) {
        if (auth == null) {
            // will result in 401 response
            LOGGER.info("No Token");
            throw new NotAuthorizedException("Bearer");
        } else if (!requestedId.equals(auth.getUser()
                .getId())) {
            // will result in 403 response
            LOGGER.info("Wrong User");
            throw new org.keycloak.services.ForbiddenException("Wrong user");
        }
    }

    private void updateUser(UserWrapper wrapper) {
        UserModel user = userProvider
                .getUserById(wrapper.getId(),
                        realm);

        if (realm.isVerifyEmail() && !user.getEmail()
                .equals(wrapper.getEmail())) {
            user.addRequiredAction(UserModel.RequiredAction.VERIFY_EMAIL);
        }

        user.setFirstName(wrapper.getGiven_name());
        user.setUsername(wrapper.getPreferred_username());
        user.setLastName(wrapper.getFamily_name());
        user.setEmail(wrapper.getEmail());
        user.setAttribute("avatar", Collections.singletonList(wrapper.getAvatar()));

    }


}
