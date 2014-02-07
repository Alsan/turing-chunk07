<!--
 ~ Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 ~
 ~ WSO2 Inc. licenses this file to you under the Apache License,
 ~ Version 2.0 (the "License"); you may not use this file except
 ~ in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~    http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing,
 ~ software distributed under the License is distributed on an
 ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ~ KIND, either express or implied.  See the License for the
 ~ specific language governing permissions and limitations
 ~ under the License.
 -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    <parent>
        <groupId>org.wso2.carbon</groupId>
    	<artifactId>bpel-feature</artifactId>
        <version>4.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>
    <artifactId>org.wso2.carbon.bpel.b4p.feature</artifactId>
    <packaging>pom</packaging>
    <name>WSO2 Carbon - BPEL BPEL4People Feature</name>
    <url>http://wso2.org</url>
    <description>This feature contains BPEL4People extension required to integrate Human Interaction functionality</description>
    <dependencies>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.wso2.maven</groupId>
                <artifactId>carbon-p2-plugin</artifactId>
                <version>${carbon.p2.plugin.version}</version>
                <executions>
                    <execution>
                        <id>4-p2-feature-generation</id>
                        <phase>package</phase>
                        <goals>
                            <goal>p2-feature-gen</goal>
                        </goals>
                        <configuration>
                            <id>org.wso2.carbon.bpel.b4p</id>
                            <propertiesFile>../etc/feature.properties</propertiesFile>
                            <adviceFile>
                                <properties>
                                    <propertyDef>org.wso2.carbon.p2.category.type:server</propertyDef>
                                    <propertyDef>org.eclipse.equinox.p2.type.group:false</propertyDef>
                                </properties>
                            </adviceFile>
                            <bundles>
                                <bundleDef>org.wso2.carbon:org.wso2.carbon.bpel.b4p</bundleDef>
                            </bundles>
                            <importBundles>
                                <importBundleDef>org.apache.ode.wso2:ode</importBundleDef>
                                <importBundleDef>asm.wso2:asm</importBundleDef>
                                <importBundleDef>cglib.wso2:cglib</importBundleDef>
                                <importBundleDef>org.apache.axis2.wso2:axis2-jibx</importBundleDef>
                                <importBundleDef>org.jibx.wso2:jibx</importBundleDef>
                                <importBundleDef>org.apache.axis2.wso2:axis2-jaxbri</importBundleDef>
                                <importBundleDef>com.sun.xml.bind.wso2:jaxb</importBundleDef>
                                <importBundleDef>org.apache.bcel.wso2:bcel</importBundleDef>
                                <importBundleDef>axion.wso2:axion</importBundleDef>
                                <importBundleDef>commons-primitives.wso2:commons-primitives</importBundleDef>
                                <importBundleDef>org.apache.geronimo.components.wso2:geronimo-connector</importBundleDef>
                                <importBundleDef>org.apache.geronimo.specs.wso2:geronimo-ejb_2.1_spec</importBundleDef>
                                <importBundleDef>org.apache.geronimo.specs.wso2:geronimo-j2ee-connector_1.5_spec</importBundleDef>
                                <importBundleDef>org.apache.geronimo.modules.wso2:geronimo-kernel</importBundleDef>
                                <importBundleDef>geronimo-spec.wso2:geronimo-spec-javamail</importBundleDef>
                                <importBundleDef>geronimo-spec.wso2:geronimo-spec-jms</importBundleDef>
                                <importBundleDef>org.apache.geronimo.components.wso2:geronimo-transaction</importBundleDef>
                                <importBundleDef>org.apache.openjpa:openjpa-osgi</importBundleDef>
                                <importBundleDef>org.apache.geronimo.specs:geronimo-jpa_2.0_spec</importBundleDef>
                                <importBundleDef>net.sf.saxon.wso2:saxon.he</importBundleDef>
                                <importBundleDef>net.sourceforge.serp.wso2:serp</importBundleDef>
                                <importBundleDef>tranql.wso2:tranql-connector</importBundleDef>
                                <importBundleDef>antlr.wso2:antlr</importBundleDef>
                                <importBundleDef>rhino.wso2:js</importBundleDef>
                            </importBundles>
                            <importFeatures>
                                <importFeatureDef>org.wso2.carbon.datasource.server:${datasources.feature.version}</importFeatureDef>
                            </importFeatures>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
