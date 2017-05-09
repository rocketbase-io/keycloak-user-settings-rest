/**
 * Created by mirco on 09.05.17.
 */
class Config {

    // aus env
    private static final String host = getHost();

    private static String getHost() {
        String env = System.getenv("KEYCLOAK_SERVER_URI");
        if (env == null) {
            env = "http://localhost";
        }
        return env;
    }

    private static final String port = "8080";

    private static final String resource = "/auth/realms/demo/profile";

    /**
     * Service URl
     */
    public static final String SERVICE = host + ":" + port + resource;

    /**
     * Auth URL
     */
    public static final String AUTH = host + ":" + port + "/auth/realms/demo/protocol/openid-connect/token";

    public static final String USER_A_ID = "a0da83a0-7c8c-4d99-a16c-05612704f067";

    public static final String USER_B_ID = "002cefe6-0bb5-4276-ab6e-bbd6143e8a14";
}
