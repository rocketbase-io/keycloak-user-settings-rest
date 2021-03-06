import io.rocketbase.keycloak.usersettings.UserSettingsResource;
import io.rocketbase.keycloak.usersettings.UserWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.models.*;
import org.keycloak.services.managers.AuthenticationManager;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.ClientErrorException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by mirco on 08.05.17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UserSettingsResource.class)

public class ProfileServiceTest {


    public static final String INVALID_USERNAME = "two";

    public static final String VALID_USERNAME = "neu";

    UserSettingsResource res;

    Map<String, UserModel> users = new HashMap<>();


    @Before
    public void init() throws Exception {
        KeycloakSession session = mock(KeycloakSession.class);
        RealmModel realm = mock(RealmModel.class);
        UserProvider userProvider = mock(UserProvider.class);

        when(realm.isEditUsernameAllowed()).thenReturn(true);

        res = spy(new UserSettingsResource(userProvider, mock(AuthenticationManager.AuthResult.class), realm));
        UserModel one = mockUser("1", "firstName", "lastName", "a@b.de", "one", Collections.emptySet(), Collections.emptySet());
        UserModel two = mockUser("2", "Zwei", "Two", "test@test.de", "two", Collections.emptySet(), Collections.emptySet());

        users.put("1", one);
        users.put("2", two);
        users.put("one", one);
        users.put("two", two);


        when(userProvider.getUserById("1", realm)).thenReturn(one);
        when(userProvider.getUserById("2", realm)).thenReturn(two);

        when(userProvider.getUserByUsername("one", realm)).thenReturn(one);
        when(userProvider.getUserByUsername("two", realm)).thenReturn(two);
        when(userProvider.getUserByUsername(VALID_USERNAME, realm)).thenReturn(null);


        PowerMockito.doNothing()
                .when(res, "authorize", Mockito.anyString());


    }


    @Test(expected = ClientErrorException.class)
    public void duplicateUserName() {
        UserWrapper wrapper = new UserWrapper(users.get("1"));
        wrapper.setPreferred_username(INVALID_USERNAME);
        res.put("1", wrapper);

    }


    @Test
    public void updateInfo() {
        UserWrapper wrapper = new UserWrapper(users.get("1"));
        wrapper.setPreferred_username(VALID_USERNAME);
        res.put("1", wrapper);
    }

    @Test
    public void getInfo() {
        UserWrapper actual = res.get("1");
        UserWrapper expected = new UserWrapper(users.get("1"));
        assertEquals(expected, actual);
    }

    private UserModel mockUser(String id, String firstName, String lastName, String email, String username, Set<RoleModel> roles, Set<GroupModel> groups) {
        UserModel model = mock(UserModel.class);

        when(model.getId()).thenReturn(id);
        when(model.getFirstName()).thenReturn(firstName);
        when(model.getLastName()).thenReturn(lastName);
        when(model.getEmail()).thenReturn(email);
        when(model.getUsername()).thenReturn(username);
        when(model.getRoleMappings()).thenReturn(roles);
        when(model.getGroups()).thenReturn(groups);

        return model;
    }

}
