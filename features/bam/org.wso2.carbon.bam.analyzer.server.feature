<?xml version="1.0" encoding="utf-8"?>
<!--
 ~ Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~      http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
-->

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    <parent>
        <groupId>org.wso2.carbon</groupId>
        <artifactId>bam-feature</artifactId>
        <version>3.2.0</version>
    </parent>

    <version>3.2.3</version>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>org.wso2.carbon.bam.analyzer.server.feature</artifactId>
    <packaging>pom</packaging>
    <name>WSO2 Carbon - BAM Analyzer Feature</name>
    <url>http://wso2.org</url>
    <description>This feature contains the bundles required for BAM Analyzer</description>

    <dependencies>
        <dependency>
            <groupId>org.wso2.carbon</groupId>
            <artifactId>org.wso2.carbon.bam.analyzer</artifactId>
	    <version>3.2.1</version>
        </dependency>
        <dependency>
            <groupId>org.wso2.carbon</groupId>
            <artifactId>org.wso2.carbon.bam.utils</artifactId>
	    <version>3.2.1</version>
        </dependency>
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
                            <id>org.wso2.carbon.bam.analyzer.server</id>
                            <propertiesFile>../etc/feature.properties</propertiesFile>
                            <adviceFile>
                                <properties>
                                    <propertyDef>org.wso2.carbon.p2.category.type:server
                                    </propertyDef>
                                    <propertyDef>org.eclipse.equinox.p2.type.group:false
                                    </propertyDef>
                                </properties>
                            </adviceFile>
                            <bundles>
                                <bundleDef>org.wso2.carbon:org.wso2.carbon.bam.analyzer</bundleDef>
                                <bundleDef>org.wso2.carbon:org.wso2.carbon.bam.utils</bundleDef>
                            </bundles>
                            <importFeatures>
                                <importFeatureDef>org.wso2.carbon.core.server:3.2.1</importFeatureDef>
				<importFeatureDef>org.wso2.carbon.cassandra.dataaccess:3.2.1</importFeatureDef>
				<importFeatureDef>org.wso2.carbon.cassandra.server:3.2.1</importFeatureDef>
				<importFeatureDef>org.wso2.carbon.task.server:3.2.0</importFeatureDef>
				<importFeatureDef>org.apache.synapse.wso2:${synapse.version}</importFeatureDef>
                            </importFeatures>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
