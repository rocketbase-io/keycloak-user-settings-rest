# KeyCloak User Settings
A provider which enables you to change user profiles via an endpoint.


# Installation
```
mvn clean install

{path to keycloak install}/jboss-cli.sh --command="module add --name=profile 
    --resources={path to jar}/profile.jar --dependencies=org.keycloak.keycloak-core,org.keycloak.keycloak-server-spi,org.keycloak.keycloak-server-spi-private,org.keycloak.keycloak-services,javax.ws.rs.api" 

```

Open ```{path to keycloak}/standalone/configuration/standalone.xml```, add: ```<provider>module:profile</provider>``` to the ```<providers>``` element.