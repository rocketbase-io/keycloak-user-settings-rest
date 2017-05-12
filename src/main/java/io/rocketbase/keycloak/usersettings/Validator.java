package io.rocketbase.keycloak.usersettings;

import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.services.validation.Validation;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

/**
 * Created by mirco on 12.05.17.
 */
public class Validator {

    private final boolean changeUserNameAllowed;

    private final UserProvider users;

    private final RealmModel realm;


    public Validator(UserProvider users, RealmModel realm) {
        this.users = users;
        this.realm = realm;
        changeUserNameAllowed = realm.isEditUsernameAllowed();

    }

    /**
     * Validates name and mail. If something is not valid, KeyCloak will answer the request with 400.
     *
     * @param user
     */
    public void validate(UserWrapper user) {
        validateUserName(user.getId(), user.getPreferred_username());
        validateEmail(user.getEmail());
    }


    /**
     * Usernames must be uniqued and are required (KeyCloak constraint). Wraps the default 500 response to a more meaningful 400.
     *
     * @param id       the id
     * @param username the new username
     */
    private void validateUserName(String id, String username) {

        if (username == null || "".equals(username.trim())) {
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

    private void validateEmail(String email) {
        if (!Validation.isEmailValid(email)) {
            throw new ClientErrorException("Email is not valid", Response.Status.BAD_REQUEST);
        }

    }

    private UserModel getUserById(String id) {
        return users
                .getUserById(id, realm);
    }

    private UserModel getUserByName(String name) {
        return users
                .getUserByUsername(name, realm);
    }

}
