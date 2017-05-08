package io.rocketbase.keycloak.usersettings;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mirco on 08.05.17.
 */
public class UserWrapper {

    private final String id;


    private final String userName;


    private final String givenName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserWrapper that = (UserWrapper) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(userName, that.userName) &&
                Objects.equal(givenName, that.givenName) &&
                Objects.equal(lastName, that.lastName) &&
                Objects.equal(email, that.email) &&
                Objects.equal(avatar, that.avatar) &&
                Objects.equal(roles, that.roles) &&
                Objects.equal(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userName, givenName, lastName, email, avatar, roles, groups);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userName", userName)
                .add("givenName", givenName)
                .add("lastName", lastName)
                .add("email", email)
                .add("avatar", avatar)
                .add("roles", roles)
                .add("groups", groups)
                .toString();
    }

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
