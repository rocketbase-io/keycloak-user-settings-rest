FROM jboss/keycloak:3.1.0.Final

COPY profile.jar /tmp/profile.jar

RUN $JBOSS_HOME/bin/jboss-cli.sh --command="module add --name=profile --resources=/tmp/profile.jar --dependencies=org.keycloak.keycloak-core,org.keycloak.keycloak-server-spi,org.keycloak.keycloak-server-spi-private,org.keycloak.keycloak-services,javax.ws.rs.api"

COPY profile.xsl /opt/jboss/keycloak/
COPY data /opt/jboss/realms/

RUN java -jar /usr/share/java/saxon.jar -s:/opt/jboss/keycloak/standalone/configuration/standalone.xml -xsl:/opt/jboss/keycloak/profile.xsl -o:/opt/jboss/keycloak/standalone/configuration/standalone.xml; rm /opt/jboss/keycloak/profile.xsl
CMD [ "-Dkeycloak.migration.action=import", "-Dkeycloak.migration.provider=dir", "-Dkeycloak.migration.dir=/opt/jboss/realms", "-Dkeycloak.migration.strategy=IGNORE_EXISTING", "-Djboss.bind.address=0.0.0.0" ]