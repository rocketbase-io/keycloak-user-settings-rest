<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- Enable new module -->
    <xsl:template xmlns:ks="urn:jboss:domain:keycloak-server:1.1" xmlns="urn:jboss:domain:keycloak-server:1.1" exclude-result-prefixes="ks"
                  match="//ks:subsystem/ks:providers/ks:provider">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
        <provider>module:profile</provider>
    </xsl:template>

</xsl:stylesheet>