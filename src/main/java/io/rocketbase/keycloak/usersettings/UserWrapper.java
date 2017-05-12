package io.rocketbase.keycloak.usersettings;


import com.google.common.base.Objects;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper for the GET request
 */
public class UserWrapper {

    private String id;

    private String preferred_username;

    private String given_name;

    private String family_name;

    private String email;

    private String avatar;

    private Collection<String> roles;

    private Collection<String> groups;

    public UserWrapper() {

    }

    public UserWrapper(UserModel model) {
        id = model.getId();
        preferred_username = model.getUsername();
        given_name = model.getFirstName();
        family_name = model.getLastName();
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
                Objects.equal(preferred_username, that.preferred_username) &&
                Objects.equal(given_name, that.given_name) &&
                Objects.equal(family_name, that.family_name) &&
                Objects.equal(email, that.email) &&
                Objects.equal(avatar, that.avatar) &&
                Objects.equal(roles, that.roles) &&
                Objects.equal(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, preferred_username, given_name, family_name, email, avatar, roles, groups);
    }

    @Override
    public String toString() {
        return "UserWrapper{" +
                "id='" + id + '\'' +
                ", preferred_username='" + preferred_username + '\'' +
                ", given_name='" + given_name + '\'' +
                ", family_name='" + family_name + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", roles=" + roles +
                ", groups=" + groups +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreferred_username() {
        return preferred_username;
    }

    public void setPreferred_username(String preferred_username) {
        this.preferred_username = preferred_username;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Collection<String> getRoles() {
        return roles;
    }


    public Collection<String> getGroups() {
        return groups;
    }


}
