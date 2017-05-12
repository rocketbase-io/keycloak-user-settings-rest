# KeyCloak User Settings
A provider which enables you to change user profiles via an endpoint.


## Installation
```
mvn clean install

{path to keycloak install}/jboss-cli.sh --command="module add --name=profile 
    --resources={path to jar}/profile.jar --dependencies=org.keycloak.keycloak-core,org.keycloak.keycloak-server-spi,org.keycloak.keycloak-server-spi-private,org.keycloak.keycloak-services,javax.ws.rs.api" 

```

Open ```{path to keycloak}/standalone/configuration/standalone.xml```, add: ```<provider>module:profile</provider>``` to the ```<providers>``` element.

## About KeyCloak extensions

The [offical documentation](https://keycloak.gitbooks.io/documentation/server_development/topics/extensions.html) only gives
little information about how to develop an extension. Here are some points we found out during development:

* you need to implement ``RealmResourceProviderFactory`` and `RealmResourceProviderFactory`` from ``org.keycloak.services.resource``
* on start, a warning will appear that internal code is used (the said interfaces)
* you need to have a file called ``org.keycloak.services.resources.RealmResourceProviderFactory`` in the folder ``resources/META-INF.services`` with the fully qualified name of your ``RealmResourceProviderFactory``
