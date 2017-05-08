package io.rocketbase.keycloak.usersettings;


import org.codehaus.jackson.annotate.JsonProperty;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mirco on 08.05.17.
 */
class UserWrapper {

    private final String id;


    private final String userName;

    @JsonProperty("given_name")
    private final String givenName;

    @JsonProperty("family_name")
    private final String lastName;

    private final String email;

    private final String avatar;

    private final Collection<String> roles;

    private final Collection<String> groups;

    public UserWrapper(UserModel model) {
        id = model.getId();
        userName = model.getUsername();
        givenName = model.getFirstName();
        lastName = model.getLastName();
        email = model.getEmail();

        roles = model.getRoleMappings()
                .stream()
                .map(RoleModel::getName)
                .collect(Collectors.toSet());

        groups = model.getGroups()
                .stream()
                .map(GroupModel::getName)
                .collect(Collectors.toSet());


        avatar = getAvatar(model);
    }

    private String getAvatar(UserModel model) {
        List<String> attr = model.getAttribute("avatar");
        String avatar = "";
        if (!attr.isEmpty()) {
            avatar = attr.get(0);
        }
        return avatar;

    }

    @JsonProperty(value = "preferred_username")
    public String getUserName() {
        return userName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public Collection<String> getGroups() {
        return groups;
    }

    public String getId() {


        return id;
    }
}
