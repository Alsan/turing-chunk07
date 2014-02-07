/*
 * Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.governance.api.util;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaxen.JaxenException;
import org.wso2.carbon.base.CarbonContextHolderBase;
import org.wso2.carbon.base.UnloadTenantTask;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.governance.api.cache.ArtifactCache;
import org.wso2.carbon.governance.api.cache.ArtifactCacheManager;
import org.wso2.carbon.governance.api.common.dataobjects.GovernanceArtifact;
import org.wso2.carbon.governance.api.common.dataobjects.GovernanceArtifactImpl;
import org.wso2.carbon.governance.api.common.util.ApproveItemBean;
import org.wso2.carbon.governance.api.common.util.CheckListItemBean;
import org.wso2.carbon.governance.api.endpoints.dataobjects.Endpoint;
import org.wso2.carbon.governance.api.endpoints.dataobjects.EndpointImpl;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.generic.dataobjects.GenericArtifactImpl;
import org.wso2.carbon.governance.api.policies.dataobjects.Policy;
import org.wso2.carbon.governance.api.policies.dataobjects.PolicyImpl;
import org.wso2.carbon.governance.api.schema.dataobjects.Schema;
import org.wso2.carbon.governance.api.schema.dataobjects.SchemaImpl;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.governance.api.wsdls.dataobjects.WsdlImpl;
import org.wso2.carbon.registry.common.AttributeSearchService;
import org.wso2.carbon.registry.common.ResourceData;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.RegistryConstants;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.config.RegistryContext;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.jdbc.handlers.RequestContext;
import org.wso2.carbon.registry.core.pagination.PaginationContext;
import org.wso2.carbon.registry.core.pagination.PaginationUtils;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.core.utils.RegistryUtils;
import org.wso2.carbon.registry.core.utils.UUIDGenerator;
import org.wso2.carbon.registry.extensions.utils.CommonUtil;
import org.wso2.carbon.utils.component.xml.config.ManagementPermission;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities used by various Governance API related functionality.
 */
public class GovernanceUtils {

    private static final Log log = LogFactory.getLog(GovernanceUtils.class);
    private static RegistryService registryService;
    //private static final String SEPARATOR = ":";
    private final static Map<Integer, List<GovernanceArtifactConfiguration>>
            artifactConfigurations = new HashMap<Integer, List<GovernanceArtifactConfiguration>>();
    private static Map<Integer, Map<String, Boolean>> lifecycleAspects =
            new HashMap<Integer, Map<String, Boolean>>();

    private static final Object ASPECT_MAP_LOCK = new Object();

    private static AttributeSearchService attributeSearchService;

    private static final ThreadLocal<Registry>  tenantGovernanceSystemRegistry  = new ThreadLocal<Registry>();
    /**
     * Setting the registry service.
     *
     * @param registryService the registryService.
     */
    public static void setRegistryService(RegistryService registryService) {
        GovernanceUtils.registryService = registryService;
    }

    // Method to register a list of governance artifact configurations.
    private static void registerArtifactConfigurations(int tenantId,
            List<GovernanceArtifactConfiguration> configurations) {
        artifactConfigurations.put(tenantId, configurations);
        CarbonContextHolderBase.registerUnloadTenantTask(new UnloadTenantTask() {
            public void register(int tenantId, Object registration) {
                // Nothing to register in here.
            }

            public void cleanup(int tenantId) {
                if (artifactConfigurations.get(tenantId) != null) {
                    artifactConfigurations.remove(tenantId);
                }
                if (lifecycleAspects.get(tenantId) != null) {
                    lifecycleAspects.remove(tenantId);
                }
            }
        });
    }

    /**
     * Query to search for governance artifacts.
     *
     * @param mediaType the media type of the artifacts to be searched for.
     * @param registry  the registry instance to run query on.
     *
     * @return the list of artifact paths.
     *
     * @throws RegistryException if the operation failed.
     */
    public static String[] findGovernanceArtifacts(String mediaType, Registry registry)
            throws RegistryException {
        String[] paths = getResultPaths(registry, mediaType);
        if (paths == null) {
            paths = new String[0];
        }
        Arrays.sort(paths, new Comparator<String>() {
            public int compare(String o1, String o2) {
                int result = RegistryUtils.getResourceName(o1)
                        .compareToIgnoreCase(RegistryUtils.getResourceName(o2));
                if (result == 0) {
                    return o1.compareToIgnoreCase(o2);
                }
                return result;
            }
        });
        return paths;
    }

    /**
     * Query to search for a governance artifact configuration.
     *
     * @param mediaType the media type of the artifact configuration.
     * @param registry  the registry instance to run query on.
     *
     * @return the artifact configuration.
     *
     * @throws RegistryException if the operation failed.
     */
    public static GovernanceArtifactConfiguration findGovernanceArtifactConfigurationByMediaType(
            String mediaType, Registry registry)
            throws RegistryException {
        List<GovernanceArtifactConfiguration> governanceArtifactConfigurations =
                findGovernanceArtifactConfigurations(registry);
        for (GovernanceArtifactConfiguration configuration : governanceArtifactConfigurations) {
            if (mediaType.equals(configuration.getMediaType())) {
                return configuration;
            }
        }
        return null;
    }

    /**
     * Query to search for a governance artifact configuration.
     *
     * @param key       the key of the artifact configuration.
     * @param registry  the registry instance to run query on.
     *
     * @return the artifact configuration.
     *
     * @throws RegistryException if the operation failed.
     */
    public static GovernanceArtifactConfiguration findGovernanceArtifactConfiguration(
            String key, Registry registry)
            throws RegistryException {
        List<GovernanceArtifactConfiguration> governanceArtifactConfigurations =
                findGovernanceArtifactConfigurations(registry);
        for (GovernanceArtifactConfiguration configuration : governanceArtifactConfigurations) {
            if (key.equals(configuration.getKey())) {
                return configuration;
            }
        }
        return null;
    }

    /**
     * Method to obtain a list of paths having resources of the given media type.
     *
     * @param registry  the registry instance to run query on.
     * @param mediaType the media type.
     * @return an array of resource paths.
     *
     * @throws GovernanceException if the operation failed.
     */
    public static String[] getResultPaths(Registry registry,
                                          String mediaType) throws GovernanceException {
        String[] result;
        String[] paginatedResult;
        try {
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("1", mediaType);
            parameter.put("query", "SELECT DISTINCT REG_PATH_ID, REG_NAME FROM REG_RESOURCE WHERE REG_MEDIA_TYPE=?");
            result = (String[]) registry.executeQuery(null, parameter).getContent();
            if (result == null || result.length == 0) {
                return new String[0];
            }
            result = removeMountPaths(result, registry);

            MessageContext messageContext = MessageContext.getCurrentMessageContext();
            if (PaginationUtils.isPaginationAnnotationFound("getPaginatedGovernanceArtifacts") &&
                    messageContext != null && PaginationUtils.isPaginationHeadersExist(messageContext)) {

                int rowCount = result.length;
                try {
                    PaginationUtils.setRowCount(messageContext, Integer.toString(rowCount));
                    PaginationContext paginationContext = PaginationUtils.initPaginationContext(messageContext);

                    int start = paginationContext.getStart();
                    int count = paginationContext.getCount();

                    int startIndex;
                    if (start == 1) {
                        startIndex = 0;
                    } else {
                        startIndex = start;
                    }
                    if (rowCount < start + count) {
                        paginatedResult = new String[rowCount - startIndex];
                        System.arraycopy(result, startIndex, paginatedResult, 0, (rowCount - startIndex));
                    } else {
                        paginatedResult = new String[count];
                        System.arraycopy(result, startIndex, paginatedResult, 0,count);
                    }
                    return paginatedResult;

                } finally {
                    PaginationContext.destroy();
                }
            }


        } catch (RegistryException e) {
            String msg = "Error in getting the result for media type: " + mediaType + ".";
            log.error(msg, e);
            throw new GovernanceException(msg, e);
        }
        return result;
    }

    // remove symbolic links in search items.
    private static String[] removeSymbolicLinks(String[] paths, Registry governanceRegistry) {
        if (paths == null) {
            return new String[0];
        }
        List<String> fixedPaths = new LinkedList<String>();
        for (String path : paths) {
            try {
                if ((governanceRegistry.get(path).getProperty(RegistryConstants.REGISTRY_LINK) ==
                        null || governanceRegistry.get(path).getProperty(
                        RegistryConstants.REGISTRY_REAL_PATH) != null) &&
                    !path.contains(RegistryConstants.SYSTEM_MOUNT_PATH)) {
                    fixedPaths.add(path);
                }
            } catch (RegistryException ignored) {
            }
        }
        return fixedPaths.toArray(new String[fixedPaths.size()]);
    }

    private static String[] removeMountPaths(String[] paths, Registry governanceRegistry) {
                if (paths == null) {
                        return new String[0];
                    }
                List<String> fixedPaths = new LinkedList<String>();
                for (String path : paths) {
                            if (!path.contains(RegistryConstants.SYSTEM_MOUNT_PATH)) {
                                    fixedPaths.add(path);
                                }
                    }
                return fixedPaths.toArray(new String[fixedPaths.size()]);
            }
    /**
     * Method to load the Governance Artifacts to be used by the API operations.
     *
     * @param registry       the registry instance used to search for artifacts.
     * @param configurations the artifact configurations to load.
     *
     * @throws RegistryException if the operation failed.
     */
    public static void loadGovernanceArtifacts(UserRegistry registry,
                                               List<GovernanceArtifactConfiguration> configurations)
            throws RegistryException {
        registerArtifactConfigurations(registry.getTenantId(), configurations);
    }
    /**
     * Method to load the Governance Artifacts to be used by the API operations.
     *
     * @param registry the registry instance used to search for artifacts.
     *
     * @throws RegistryException if the operation failed.
     */
    public static void loadGovernanceArtifacts(UserRegistry registry) throws RegistryException {
        loadGovernanceArtifacts(registry, Collections.unmodifiableList(findGovernanceArtifactConfigurations(registry)));
    }
    public static GovernanceArtifactConfiguration getGovernanceArtifactConfiguration(String elementString){
        GovernanceArtifactConfiguration configuration = null;
        try {
            OMElement configElement = AXIOMUtil.stringToOM(elementString);
            if (configElement != null) {
                configuration  = new GovernanceArtifactConfiguration();
                OMElement artifactNameAttributeElement = configElement.getFirstChildWithName(
                        new QName("nameAttribute"));
                if (artifactNameAttributeElement != null) {
                    configuration.setArtifactNameAttribute(
                            artifactNameAttributeElement.getText());
                }
                OMElement artifactNamespaceAttributeElement =
                        configElement.getFirstChildWithName(
                                new QName("namespaceAttribute"));
                if (artifactNamespaceAttributeElement != null) {
                    configuration.setArtifactNamespaceAttribute(
                            artifactNamespaceAttributeElement.getText());
                } else if (Boolean.toString(false).equals(
                        configElement.getAttributeValue(new QName("hasNamespace")))) {
                    configuration.setArtifactNamespaceAttribute(null);
                } else {
                    configuration.setHasNamespace(true);
                }
                OMElement artifactElementRootElement = configElement.getFirstChildWithName(
                        new QName("elementRoot"));
                if (artifactElementRootElement != null) {
                    configuration.setArtifactElementRoot(
                            artifactElementRootElement.getText());
                }
                OMElement artifactElementNamespaceElement = configElement.getFirstChildWithName(
                        new QName("elementNamespace"));
                if (artifactElementNamespaceElement != null) {
                    configuration.setArtifactElementNamespace(
                            artifactElementNamespaceElement.getText());
                }
                configuration.setKey(configElement.getAttributeValue(new QName("shortName")));
                configuration.setSingularLabel(
                        configElement.getAttributeValue(new QName("singularLabel")));
                configuration.setPluralLabel(
                        configElement.getAttributeValue(new QName("pluralLabel")));
                configuration.setMediaType(
                        configElement.getAttributeValue(new QName("type")));
                configuration.setExtension(
                        configElement.getAttributeValue(new QName("fileExtension")));

                String iconSetString = configElement.getAttributeValue(new QName("iconSet"));
                if (iconSetString != null) {
                    configuration.setIconSet(Integer.parseInt(iconSetString));
                }

                OMElement pathExpressionElement = configElement.getFirstChildWithName(
                        new QName("storagePath"));
                if (pathExpressionElement != null) {
                    configuration.setPathExpression(pathExpressionElement.getText());
                } else {
                    configuration.setPathExpression("/@{name}");
                }

                OMElement lifecycleElement = configElement.getFirstChildWithName(
                        new QName("lifecycle"));
                if (lifecycleElement != null) {
                    configuration.setLifecycle(lifecycleElement.getText());
                }

                OMElement contentDefinition = configElement.getFirstChildWithName(
                        new QName("content"));
                if (contentDefinition != null) {
                    String href = contentDefinition.getAttributeValue(new QName("href"));
                    if (href != null) {
                        configuration.setContentURL(href);
                    }
                    configuration.setContentDefinition(contentDefinition);
                }
                OMElement associationDefinitions = configElement.getFirstChildWithName(
                        new QName("relationships"));
                if (associationDefinitions != null) {
                    List<Association> associations =
                            new LinkedList<Association>();
                    Iterator associationElements =
                            associationDefinitions.getChildrenWithName(
                                    new QName("association"));
                    while (associationElements.hasNext()) {
                        OMElement associationElement = (OMElement) associationElements.next();
                        String type = associationElement.getAttributeValue(new QName("type"));
                        String source =
                                associationElement.getAttributeValue(new QName("source"));
                        String target =
                                associationElement.getAttributeValue(new QName("target"));
                        associations.add(new Association(source, target, type));
                    }
                    associationElements =
                            associationDefinitions.getChildrenWithName(
                                    new QName("dependency"));
                    while (associationElements.hasNext()) {
                        OMElement associationElement = (OMElement) associationElements.next();
                        String source =
                                associationElement.getAttributeValue(new QName("source"));
                        String target =
                                associationElement.getAttributeValue(new QName("target"));
                        associations.add(new Association(source, target, "depends"));
                    }
                    configuration.setRelationshipDefinitions(associations.toArray(
                            new Association[associations.size()]));
                }
                OMElement uiConfigurations = configElement.getFirstChildWithName(
                        new QName("ui"));
                if (uiConfigurations != null) {
                    configuration.setUIConfigurations(uiConfigurations);
                    OMElement uiListConfigurations = uiConfigurations.getFirstChildWithName(
                            new QName("list"));
                    if (uiListConfigurations != null) {
                        configuration.setUIListConfigurations(uiListConfigurations);
                    }
                }
                OMElement uiPermissions = configElement.getFirstChildWithName(
                        new QName("permissions"));
                if (uiPermissions != null) {
                    Iterator permissionElements =
                            uiPermissions.getChildrenWithName(
                                    new QName("permission"));
                    List<ManagementPermission> managementPermissions =
                            new LinkedList<ManagementPermission>();
                    while (permissionElements.hasNext()) {
                        OMElement permissionElement = (OMElement) permissionElements.next();
                        OMElement nameElement =
                                permissionElement.getFirstChildWithName(
                                        new QName("name"));
                        String name = (nameElement != null) ? nameElement.getText() : null;

                        OMElement idElement =
                                permissionElement.getFirstChildWithName(
                                        new QName("id"));
                        String id = (idElement != null) ? idElement.getText() : null;
                        if (name != null && id != null) {
                            managementPermissions.add(new ManagementPermission(name, id));
                        }
                    }
                    configuration.setUIPermissions(managementPermissions.toArray(
                            new ManagementPermission[managementPermissions.size()]));
                } else {
                    // if no permission definitions were present, define the default ones.
                    List<ManagementPermission> managementPermissions =
                            new LinkedList<ManagementPermission>();
                    String idPrefix = "/permission/admin/manage/resources/govern/" +
                            configuration.getKey();
                    managementPermissions.add(
                            new ManagementPermission(configuration.getPluralLabel(), idPrefix));
                    managementPermissions.add(
                            new ManagementPermission("Add", idPrefix + "/add"));
                    managementPermissions.add(
                            new ManagementPermission("List", idPrefix + "/list"));
                    managementPermissions.add(
                            new ManagementPermission(configuration.getPluralLabel(),
                                    "/permission/admin/configure/governance/" +
                                            configuration.getKey() + "-ui"));
                    configuration.setUIPermissions(managementPermissions.toArray(
                            new ManagementPermission[managementPermissions.size()]));
                }
            }
        } catch (XMLStreamException ignored) {
        } catch (NumberFormatException ignored) {
        }
        return configuration;
    }

    /**
     * Method to locate Governance Artifact configurations.
     *
     * @param registry the registry instance to run query on.
     * @return an array of resource paths.
     *
     * @throws GovernanceException if the operation failed.
     */
    public static List<GovernanceArtifactConfiguration> findGovernanceArtifactConfigurations(
            Registry registry) throws RegistryException {
        String[] artifactConfigurations = findGovernanceArtifacts(
                GovernanceConstants.GOVERNANCE_ARTIFACT_CONFIGURATION_MEDIA_TYPE, registry);
        List<GovernanceArtifactConfiguration> configurations =
                new LinkedList<GovernanceArtifactConfiguration>();
        for (String artifactConfiguration : artifactConfigurations) {
            Resource resource = registry.get(artifactConfiguration);
            Object content = resource.getContent();
            String elementString;
            if (content instanceof String) {
                elementString = (String) content;
            } else {
                elementString = RegistryUtils.decodeBytes((byte[])content);
            }
            configurations.add(getGovernanceArtifactConfiguration(elementString));
        }
        return configurations;
    }

    public static void setTenantGovernanceSystemRegistry(final int tenantId) throws RegistryException {
        if (registryService != null) {
            tenantGovernanceSystemRegistry.set(
                    registryService.getGovernanceSystemRegistry(tenantId));
        }
    }

     public static void unsetTenantGovernanceSystemRegistry() throws RegistryException {
         tenantGovernanceSystemRegistry.remove();
    }
    /**
     * Returns the system governance registry.
     *
     * @param registry the user registry.
     * @return the system registry.
     * @throws RegistryException throws if an error occurs
     */
    public static Registry getGovernanceSystemRegistry(Registry registry) throws RegistryException {

        if(tenantGovernanceSystemRegistry.get() != null) {
            return  tenantGovernanceSystemRegistry.get();
        }

        if (registryService == null) {
            return null;
        }
        UserRegistry userRegistry;
        if (!(registry instanceof UserRegistry)) {
            return null;
        }
        userRegistry = (UserRegistry) registry;
        return registryService.getGovernanceSystemRegistry(userRegistry.getTenantId());
    }

    /**
     * Obtains the governance user registry from the given root registry instance. This is useful
     * when creating a governance user registry out of a remote client registry instance.
     *
     * @param registry the remote client registry instance.
     * @param username the name of the user to connect as.
     *
     * @return the system registry.
     * @throws RegistryException throws if an error occurs
     */
    @SuppressWarnings("unused")
    public static Registry getGovernanceUserRegistry(Registry registry, String username)
            throws RegistryException {
        if (RegistryContext.getBaseInstance() == null) {
            RegistryContext.getBaseInstance(null, false);
        }
        return new UserRegistry(username, MultitenantConstants.SUPER_TENANT_ID, registry, null,
                RegistryConstants.GOVERNANCE_REGISTRY_BASE_PATH);
    }

    /**
     * Obtains the governance user registry from the given root registry instance. This is useful
     * when creating a tenant aware governance user registry out of a remote client registry instance.
     *
     * @param registry registry the remote client registry instance.
     * @param username username the name of the user to connect as.
     * @param tenantId tenant id
     *
     * @return the system registry
     * @throws RegistryException throws if an error occurs
     */
    public static Registry getGovernanceUserRegistry(Registry registry, String username, int tenantId)
            throws RegistryException {
        if (RegistryContext.getBaseInstance() == null) {
            RegistryContext.getBaseInstance(null, false);
        }
        return new UserRegistry(username, tenantId, registry, null,
                RegistryConstants.GOVERNANCE_REGISTRY_BASE_PATH);
    }

    public static String parameterizeString(RequestContext requestContext, String parameterString){
        String parameterizedString = parameterString;
		Pattern pattern = Pattern.compile("\\{@(\\w)*\\}");
		Matcher matcher = pattern.matcher(parameterString);
		GovernanceArtifact governanceArtifact = null;
		Registry registry = requestContext.getRegistry();
		String resourcePath = requestContext.getResourcePath().getPath();
		Set<String> matchSet = new HashSet<String>();

        while (matcher.find()) {
			matchSet.add(matcher.group());
		}

		Iterator<String> iter = matchSet.iterator();
		while (iter.hasNext()) {
			String current = iter.next();
			String name = current.substring(2,current.length()-1);
            //To replace special values such as {@resourcePath}
            if(name.equals("resourcePath")){
                parameterizedString = parameterizedString.replaceAll("\\"+current.replace("}", "\\}"), resourcePath);
            }

			try {
				governanceArtifact = GovernanceUtils.retrieveGovernanceArtifactByPath(requestContext.getSystemRegistry(), resourcePath);
				if (governanceArtifact!=null&&governanceArtifact.getAttribute(name)!=null) {
					parameterizedString = parameterizedString.replaceAll("\\"+current.replace("}", "\\}"), governanceArtifact.getAttribute(name));
				}else if(registry.get(resourcePath).getProperty(name)!=null) {
					parameterizedString = parameterizedString.replaceAll("\\"+current.replace("}", "\\}"), registry.get(resourcePath).getProperty(name));
				}else{
					log.error("Unable to locate the given value in properties or attributes");
				}
			} catch (RegistryException e) {
				log.error(e.getMessage(),e);
			}

			 

		}
		
		return parameterizedString;

	}
    
    /**
     * Method to remove a governance artifact from the registry.
     *
     * @param registry   the registry instance.
     * @param artifactId the identifier of the artifact.
     * @throws GovernanceException if the operation failed.
     */
    public static void removeArtifact(Registry registry, String artifactId)
            throws GovernanceException {

        try {
            String path = getArtifactPath(registry,artifactId);
            if (registry.resourceExists(path)) {
                registry.delete(path);
            }
            ArtifactCache artifactCache =
                    ArtifactCacheManager.getCacheManager().getTenantArtifactCache(((UserRegistry) registry).getTenantId());
            if(artifactCache!=null && path!=null){
                artifactCache.invalidateArtifact(path);
            }
        } catch (RegistryException e) {
            String msg = "Error in deleting the the artifact id:" + artifactId + ".";
            log.error(msg, e);
            throw new GovernanceException(msg, e);
        }
    }

    /**
     * Method to obtain the artifact path of a governance artifact on the registry.
     *
     * @param registry   the registry instance.
     * @param artifactId the identifier of the artifact.
     * @return the artifact path.
     * @throws GovernanceException if the operation failed.
     */
    public static String getArtifactPath(Registry registry, String artifactId)
            throws GovernanceException {

        try {
            String sql = "SELECT REG_PATH_ID, REG_NAME FROM REG_RESOURCE WHERE REG_UUID = ?";

            String[] result;
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("1", artifactId);
            parameter.put("query", sql);
            result = registry.executeQuery(null, parameter).getChildren();

            if (result != null && result.length == 1) {
                return result[0];
            }
            return null;
        } catch (RegistryException e) {
            String msg = "Error in getting the path from the registry. Execute query failed with message : "
                    + e.getMessage();
            log.error(msg, e);
            throw new GovernanceException(msg, e);
        }
    }

    /**
     * Retrieve all the governance artifact paths which associated with the given lifecycle
     *
     * @param registry  registry instance
     * @param lcName    lifecycle name
     * @param mediaType  mediatype of the artifacts
     * @return
     * @throws GovernanceException if the operation failed.
     */
    public static String[] getAllArtifactPathsByLifecycle(Registry registry, String lcName, String mediaType) throws GovernanceException {
        String sql = "SELECT R.REG_PATH_ID, R.REG_NAME FROM REG_RESOURCE R, REG_PROPERTY PP, " +
                "REG_RESOURCE_PROPERTY RP WHERE R.REG_VERSION=RP.REG_VERSION AND RP.REG_PROPERTY_ID=PP.REG_ID " +
                "AND PP.REG_NAME = ?  AND  PP.REG_VALUE = ? AND R.REG_MEDIA_TYPE = ?";
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("1", "registry.LC.name");
        parameter.put("2", lcName);
        parameter.put("3", mediaType);
        parameter.put("query", sql);

        try {
            return (String[]) registry.executeQuery(null, parameter).getContent();
        } catch (RegistryException e) {
            String msg = "Error occured while executing custom query";
            throw new GovernanceException(msg, e);
        }
    }

    /**
     * Retrieve all the governance artifact paths which associated with the given lifecycle in the given lifecycle state
     *
     * @param registry  registry instance
     * @param lcName    lifecycle name
     * @param lcState   lifecycle state
     * @param mediaType  mediatype of the artifacts
     * @return
     * @throws GovernanceException if the operation failed.
     */
    public static String[] getAllArtifactPathsByLifecycleState(
            Registry registry, String lcName, String lcState, String mediaType) throws GovernanceException {
        String sql = "SELECT R.REG_PATH_ID, R.REG_NAME FROM REG_RESOURCE R, REG_PROPERTY PP, " +
                "REG_RESOURCE_PROPERTY RP WHERE R.REG_VERSION=RP.REG_VERSION AND RP.REG_PROPERTY_ID=PP.REG_ID " +
                "AND PP.REG_NAME = ?  AND  PP.REG_VALUE = ? AND R.REG_MEDIA_TYPE = ?";
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("1", "registry.lifecycle." + lcName + ".state");
        parameter.put("2", lcState);
        parameter.put("3", mediaType);
        parameter.put("query", sql);

        try {
            return (String[]) registry.executeQuery(null, parameter).getContent();
        } catch (RegistryException e) {
            String msg = "Error occured while executing custom query";
            throw new GovernanceException(msg, e);
        }
    }

    /**
     * Method to obtain the value of a governance attribute.
     *
     * @param element   the payload element.
     * @param name      the attribute name.
     * @param namespace the namespace of the payload element.
     *
     * @return the value of the attribute by the given name if it exists or an empty string.
     */
    public static String getAttributeValue(OMElement element, String name, String namespace) {
        String[] parts = name.split("_");
        OMElement attributeElement = element;
        for (String part : parts) {
            attributeElement = attributeElement.getFirstChildWithName(new QName(namespace, part));
            if (attributeElement == null) {
                return "";
            }
        }
        return attributeElement.getText();
    }

    /**
     * @deprecated
     * Method to obtain all indexed governance artifact identifiers on the provided registry
     * instance.
     * @param registry the registry instance.
     * @return list of governance artifact identifiers.
     * @throws GovernanceException if the operation failed.
     */
    public static String[] getAllArtifactIds(Registry registry)
            throws GovernanceException {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated
     * Method to obtain all indexed governance artifacts on the provided registry instance.
     * @param registry the registry instance.
     * @return list of governance artifacts
     * @throws GovernanceException if the operation failed.
     */
    public static GovernanceArtifact[] getAllArtifacts(Registry registry)
            throws GovernanceException{
        throw new UnsupportedOperationException();
    }

    /**
     * Method to obtain a governance artifact on the registry.
     *
     * @param registry   the registry instance.
     * @param artifactId the identifier of the artifact.
     * @return the artifact.
     * @throws GovernanceException if the operation failed.
     */
    public static GovernanceArtifact retrieveGovernanceArtifactById(Registry registry,
                                                                    String artifactId)
            throws GovernanceException {
        String artifactPath = getArtifactPath(registry, artifactId);
        if (artifactPath == null) {
            String msg = "Governance artifact is not found for id: " + artifactId + ".";
            if (log.isDebugEnabled()) {
                log.debug(msg);
            }
            return null;
        }
        return retrieveGovernanceArtifactByPath(registry, artifactPath);
    }

    /**
     * Method to obtain a governance artifact on the registry by the artifact path.
     *
     * @param registry     the registry instance.
     * @param artifactPath the path of the artifact.
     * @return the artifact.
     * @throws GovernanceException if the operation failed.
     */
    public static GovernanceArtifact retrieveGovernanceArtifactByPath(Registry registry,
                                                                      String artifactPath)
            throws GovernanceException {
        UserRegistry userRegistry = (UserRegistry) registry;
        String currentUser = userRegistry.getUserName();
        ArtifactCache artifactCache =
                ArtifactCacheManager.getCacheManager().getTenantArtifactCache(userRegistry.getTenantId());
        if (artifactPath != null && artifactCache !=null) {
            GovernanceArtifact governanceArtifact = artifactCache.getArtifact(artifactPath);
            if (governanceArtifact != null) {
                return governanceArtifact;
            }
        }

        String artifactLC;
        String artifactLCState = null;
        try {
            Resource artifactResource;
            if (registry.resourceExists(artifactPath)) {
                artifactResource = registry.get(artifactPath);
                artifactLC =  artifactResource.getProperty("registry.LC.name");

                if(artifactLC != null){
                    artifactLCState = artifactResource.getProperty("registry.lifecycle." + artifactLC+".state");
                }
            } else {
                // if the artifact path doesn't exist we are returning null.
                if (log.isDebugEnabled()) {
                    String msg = "The artifact path doesn't exists at " + artifactPath + ".";
                    log.debug(msg);
                }
                return null;
            }
            String artifactId =
                    artifactResource.getUUID();
            String mediaType = artifactResource.getMediaType();
            if (GovernanceConstants.WSDL_MEDIA_TYPE
                    .equals(mediaType)) {
                Wsdl wsdl = new WsdlImpl(artifactId, registry);
                ((WsdlImpl) wsdl).setLcName(artifactLC);
                ((WsdlImpl)wsdl).setLcState(artifactLCState);
               ((WsdlImpl)wsdl).setArtifactPath(artifactPath);
                if (artifactCache != null) {
                    artifactCache.addArtifact(artifactPath,wsdl);
                }
                return wsdl;
            } else if (GovernanceConstants.SCHEMA_MEDIA_TYPE
                    .equals(mediaType)) {
                Schema schema = new SchemaImpl(artifactId, registry);
                ((SchemaImpl)schema).setLcName(artifactLC);
                ((SchemaImpl)schema).setLcState(artifactLCState);
                ((SchemaImpl)schema).setArtifactPath(artifactPath);
                if (artifactCache != null) {
                    artifactCache.addArtifact(artifactPath,schema);
                }
                return schema;
            } else if (GovernanceConstants.POLICY_XML_MEDIA_TYPE
                    .equals(mediaType)) {
                Policy policy = new PolicyImpl(artifactId, registry);
                ((PolicyImpl)policy).setLcName(artifactLC);
                ((PolicyImpl)policy).setLcState(artifactLCState);
                ((PolicyImpl)policy).setArtifactPath(artifactPath);
                if(artifactCache!=null){
                    artifactCache.addArtifact(artifactPath,policy);
                }
                return policy;
            } else if (GovernanceConstants.ENDPOINT_MEDIA_TYPE
                    .equals(mediaType)) {
                Endpoint endpoint = new EndpointImpl(artifactId, registry);
                ((EndpointImpl)endpoint).setLcName(artifactLC);
                ((EndpointImpl)endpoint).setLcState(artifactLCState);
                ((EndpointImpl)endpoint).setArtifactPath(artifactPath);
                if(artifactCache!=null){
                    artifactCache.addArtifact(artifactPath,endpoint);
                }
                return endpoint;
            } else if (mediaType != null && mediaType.matches("application/[a-zA-Z0-9.+-]+")) {
                if (registry instanceof UserRegistry) {
                    List<GovernanceArtifactConfiguration> configurations =
                            artifactConfigurations.get(((UserRegistry) registry).getTenantId());
                    if (configurations != null) {
                        for (GovernanceArtifactConfiguration configuration :
                                configurations) {
                            if (mediaType.equals(configuration.getMediaType())) {
                                GenericArtifactImpl artifact;
                                if (mediaType.matches("application/vnd\\.[a-zA-Z0-9.-]+\\+xml")) {
                                    byte[] contentBytes = (byte[]) artifactResource.getContent();
                                    if (contentBytes == null) {
                                        throw new GovernanceException(
                                                "Unable to read payload of governance artifact " +
                                                        "at path: " + artifactPath);
                                    }
                                    OMElement contentElement = buildOMElement(contentBytes);
                                    artifact = new GenericArtifactImpl(
                                            artifactId, contentElement,
                                            configuration.getArtifactNameAttribute(),
                                            configuration.getArtifactNamespaceAttribute(),
                                            configuration.getArtifactElementNamespace(),
                                            configuration.getMediaType());
                                    artifact.associateRegistry(registry);
                                    artifact.setArtifactPath(artifactPath);
                                } else {
                                    artifact = new GenericArtifactImpl(artifactId, registry);
                                }
                                artifact.setLcState(artifactLCState);
                                artifact.setLcName(artifactLC);
                                if(artifactCache!=null){
                                    artifactCache.addArtifact(artifactPath,artifact);
                                }
                                return artifact;
                            }
                        }
                    }
                }
            }
            /*else if (GovernanceConstants.PEOPLE_MEDIA_TYPE.
                    equals(artifactResource.getMediaType())) {
                // it is a peopleArtifact
                byte[] contentBytes = (byte[]) artifactResource.getContent();
                OMElement contentElement = null;
                if (contentBytes != null) {
                    contentElement = buildOMElement(contentBytes);
                }
                String peopleGroup = CommonUtil.getPeopleGroup(contentElement);
                PeopleArtifact peopleArtifact = null;
                switch (PeopleGroup.valueOf(peopleGroup.toUpperCase())) {
                    case ORGANIZATION:
                        peopleArtifact = new Organization(artifactId, contentElement);
                        break;
                    case DEPARTMENT:
                        peopleArtifact = new Department(artifactId, contentElement);
                        break;
                    case PROJECT_GROUP:
                        peopleArtifact = new ProjectGroup(artifactId, contentElement);
                        break;
                    case PERSON:
                        peopleArtifact = new Person(artifactId, contentElement);
                        break;
                    default:
                        assert false;
                }
                peopleArtifact.associateRegistry(registry);
                return peopleArtifact;
            }*/

        } catch (RegistryException e) {
            String msg =
                    "Error in retrieving governance artifact by path. path: " + artifactPath + ".";
            log.error(msg, e);
            throw new GovernanceException(msg, e);
        }
        return null;
    }

    /**
     * Extracts all CheckListItemBeans from the resource
     * and update the lifecycle name and state of the artifact
     *
     * @param artifactResource resource related to the artifact
     * @param artifact artifact which related to the resource
     * @return CheckListItemBean array extracted from the resource
     * @throws GovernanceException GovernanceException  if the operation failed.
     */
    public static CheckListItemBean[] getAllCheckListItemBeans(Resource artifactResource,
                                                               GovernanceArtifact artifact) throws GovernanceException {
        String artifactLC = artifactResource.getProperty("registry.LC.name");
        if (artifactLC == null) {
            throw new GovernanceException("No lifecycle associated with the artifact path " +
                    artifactResource.getPath());
        }
        String artifactLCState = artifactResource.getProperty("registry.lifecycle." + artifactLC + ".state");
        ((GovernanceArtifactImpl)artifact).setLcState(artifactLCState);
        ArrayList<CheckListItemBean> checkListItemList = new ArrayList<CheckListItemBean>();
        Properties lifecycleProps = artifactResource.getProperties();
        Set propertyKeys = lifecycleProps.keySet();
        for (Object propertyObj : propertyKeys) {
            String propertyKey = (String) propertyObj;

            String checkListPrefix = "registry.custom_lifecycle.checklist.";
            String checkListSuffix = ".item";

            if (propertyKey.startsWith(checkListPrefix) && propertyKey.endsWith(checkListSuffix)) {
                List<String> propValues = (List<String>) lifecycleProps.get(propertyKey);
                CheckListItemBean checkListItem = new CheckListItemBean();
                if (propValues != null && propValues.size() > 2) {
                    for (String param : propValues) {
                        if ((param.startsWith("status:"))) {
                            checkListItem.setStatus(param.substring(7));
                        } else if ((param.startsWith("name:"))) {
                            checkListItem.setName(param.substring(5));
                        } else if ((param.startsWith("value:"))) {
                            checkListItem.setValue(Boolean.parseBoolean(param.substring(6)));
                        } else if ((param.startsWith("order:"))) {
                            checkListItem.setOrder(Integer.parseInt(param.substring(6)));
                        }
                    }
                }
                checkListItemList.add(checkListItem);
            }
        }
        CheckListItemBean[] checkListItemBeans = new CheckListItemBean[checkListItemList.size()];
        if (checkListItemBeans.length > 0) {
            for (CheckListItemBean checkListItemBean : checkListItemList) {
                checkListItemBeans[checkListItemBean.getOrder()] = checkListItemBean;
            }
            return checkListItemBeans;
        }
        return null;
    }

    /**
     * Extracts all ApproveItemBeans from the resource
     *
     * @param currentUser current registry user
     * @param artifactResource resource related to the artifact
     * @param artifact artifact related to the resource
     * @return ApproveItemBean array extracted from the resource
     * @throws GovernanceException if the operation failed.
     */
    public static ApproveItemBean[] getAllApproveItemBeans(
            String currentUser, Resource artifactResource, GovernanceArtifact artifact) throws GovernanceException {
        String artifactLC = artifactResource.getProperty("registry.LC.name");
        if (artifactLC == null) {
            throw new GovernanceException("No lifecycle associated with the artifact path " +
                    artifactResource.getPath());
        }
        String artifactLCState = artifactResource.getProperty("registry.lifecycle." + artifactLC + ".state");
        ((GovernanceArtifactImpl)artifact).setLcState(artifactLCState);
        ArrayList<ApproveItemBean> approveItemList = new ArrayList<ApproveItemBean>();
        Properties lifecycleProps = artifactResource.getProperties();
        Set propertyKeys = lifecycleProps.keySet();
        for (Object propertyObj : propertyKeys) {
            String propertyKey = (String) propertyObj;

            String votingPrefix = "registry.custom_lifecycle.votes.";
            String votingSuffix = ".vote";

            if (propertyKey.startsWith(votingPrefix) && propertyKey.endsWith(votingSuffix)) {
                List<String> propValues = (List<String>) lifecycleProps.get(propertyKey);
                ApproveItemBean approveItemBean = new ApproveItemBean();
                if (propValues != null && propValues.size() > 2) {
                    for (String param : propValues) {
                        if ((param.startsWith("status:"))) {
                            approveItemBean.setStatus(param.substring(7));
                        } else if ((param.startsWith("name:"))) {
                            approveItemBean.setName(param.substring(5));
                        } else if ((param.startsWith("votes:"))) {
                            approveItemBean.setRequiredVotes(Integer.parseInt(param.substring(6)));
                        } else if ((param.startsWith("current:"))) {
                            approveItemBean.setVotes(Integer.parseInt(param.substring(8)));
                        } else if ((param.startsWith("order:"))) {
                            approveItemBean.setOrder(Integer.parseInt(param.substring(6)));
                        } else if ((param.startsWith("users:"))) {
                            String users = param.substring(6);
                            if (!users.equals("")) {
                                List<String> votedUsers = Arrays.asList(users.split(","));
                                approveItemBean.setVoters(votedUsers);
                                approveItemBean.setValue(votedUsers.contains(currentUser));
                            }
                        }
                    }
                }
                approveItemList.add(approveItemBean);
            }
        }

        ApproveItemBean[] approveItemBeans = new ApproveItemBean[approveItemList.size()];
        if (approveItemBeans.length > 0) {
            for (ApproveItemBean approveItemBean : approveItemList) {
                approveItemBeans[approveItemBean.getOrder()] = approveItemBean;
            }
            return approveItemBeans;
        }
        return null;
    }

    /*public static String retrieveGovernanceArtifactPath(Registry registry,
                                                      String artifactId) throws GovernanceException {
        try {
            Resource govIndexResource = registry.get(GovernanceConstants.GOVERNANCE_ARTIFACT_INDEX_PATH);
            return govIndexResource.getProperty(artifactId);
        } catch (RegistryException e) {
            String msg = "Error in adding an entry for the governance artifact. uuid: " + artifactId + ".";
            log.error(msg);
            throw new GovernanceException(msg, e);
        }
    }*/

    /**
     * Method to register a governance artifact.
     *
     * @param registry     the registry instance.
     * @param artifactId   the identifier of the artifact.
     * @param artifactPath the path of the artifact.
     * @throws GovernanceException if the operation failed.
     */
/*
    public static void addGovernanceArtifactEntry(Registry registry,
                                                  String artifactId,
                                                  String artifactPath) throws GovernanceException {
        try {
            Registry systemGovernanceRegistry = getGovernanceSystemRegistry(registry);
            if (systemGovernanceRegistry == null) {
                systemGovernanceRegistry = registry;
            }
            Resource govIndexResource;
            if (systemGovernanceRegistry.resourceExists(
                    GovernanceConstants.GOVERNANCE_ARTIFACT_INDEX_PATH)) {
                govIndexResource = systemGovernanceRegistry.get(
                        GovernanceConstants.GOVERNANCE_ARTIFACT_INDEX_PATH);
            } else {
                govIndexResource = systemGovernanceRegistry.newResource();
            }
            govIndexResource.setProperty(artifactId, artifactPath);
            govIndexResource.setVersionableChange(false);
            systemGovernanceRegistry.put(GovernanceConstants.GOVERNANCE_ARTIFACT_INDEX_PATH,
                    govIndexResource);
        } catch (RegistryException e) {
            String msg =
                    "Error in adding an entry for the governance artifact. path: " + artifactPath +
                            ", uuid: " + artifactId + ".";
            log.error(msg);
            throw new GovernanceException(msg, e);
        }
    }
*/

    /**
     * Method to build an AXIOM element from a byte stream.
     *
     * @param content the stream of bytes.
     * @return the AXIOM element.
     * @throws GovernanceException if the operation failed.
     */
    public static OMElement buildOMElement(byte[] content) throws RegistryException {
        XMLStreamReader parser;
        try {
        	XMLInputFactory factory = XMLInputFactory.newInstance();
        	factory.setProperty(XMLInputFactory.IS_COALESCING, new Boolean(true));
            parser = factory.createXMLStreamReader(new StringReader(
                    RegistryUtils.decodeBytes(content)));
        } catch (XMLStreamException e) {
            String msg = "Error in initializing the parser to build the OMElement.";
            log.error(msg, e);
            throw new GovernanceException(msg, e);
        }

        //create the builder
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        //get the root element (in this case the envelope)

        return builder.getDocumentElement();
    }

    /**
     * Method to serialize an XML element into a string.
     *
     * @param element the XML element.
     * @return the corresponding String representation
     * @throws GovernanceException if the operation failed.
     */
    public static String serializeOMElement(OMElement element) throws GovernanceException {
        try {
            return element.toStringWithConsume();
        } catch (XMLStreamException e) {
            String msg = "Error in serializing the OMElement.";
            log.error(msg, e);
            throw new GovernanceException(msg, e);
        }
    }

    /**
     * Method to convert the expression specified for storing the path with corresponding values
     * where the artifact is stored.
     *
     * @param pathExpression the expression specified for storing the path
     * @param artifact       the governance artifact
     * @param storagePath    the storage path of the artifact
     * @return               the path with corresponding values where the artifact is stored
     *
     * @throws GovernanceException if the operation failed.
     */
    public static String getPathFromPathExpression(String pathExpression,
                                                   GovernanceArtifact artifact,
                                                   String storagePath) throws GovernanceException {
        return getPathFromPathExpression(
                pathExpression.replace("@{storagePath}", storagePath).replace("@{uuid}",
                        artifact.getId()), artifact);
    }

    /**
     * Method to convert the expression specified for storing the path with corresponding values
     * where the artifact is stored.
     *
     * @param pathExpression the expression specified for storing the path
     * @param artifact       the governance artifact
     * @return               the path with corresponding values where the artifact is stored
     *
     * @throws GovernanceException if the operation failed.
     */
    public static String getPathFromPathExpression(String pathExpression,
                                                   GovernanceArtifact artifact)
            throws GovernanceException {
        String output = replaceNameAndNamespace(pathExpression, artifact);
        String[] elements = output.split("@");
        for (int i = 1; i < elements.length; i++) {
            if (elements[i].indexOf("}") > 0 && elements[i].indexOf("{") == 0) {
                String key = elements[i].split("}")[0].substring(1);
                String artifactAttribute = artifact.getAttribute(key);
                if (artifactAttribute != null) {
                    output = output.replace("@{" + key + "}", artifactAttribute);
                } else {
                    String msg = "Value for required attribute " + key + " found empty.";
                    log.error(msg);
                    throw new GovernanceException(msg);
                }
            }
        }
        return output;
    }

    /**
     * Method to compare the old and new artifact paths
     *
     * @param pathExpression the expression specified for storing the path
     * @param newArtifact updated artifact
     * @param oldArtifact  existing artifact
     * @return  whether the paths are same for old artifact and new artifact
     * @throws GovernanceException if the operation failed.
     */
    public static boolean hasSamePath(String pathExpression,
                                                   GovernanceArtifact newArtifact, GovernanceArtifact oldArtifact)
            throws GovernanceException {
        String output = replaceNameAndNamespace(pathExpression, newArtifact);
        String[] elements = output.split("@");
        for (int i = 1; i < elements.length; i++) {
            if (elements[i].indexOf("}") > 0 && elements[i].indexOf("{") == 0) {
                String key = elements[i].split("}")[0].substring(1);
                String oldArtifactAttribute = oldArtifact.getAttribute(key);
                String newArtifactAttribute = newArtifact.getAttribute(key);
                if (newArtifactAttribute != null) {
                    if(newArtifactAttribute.equals(oldArtifactAttribute)){
                        continue;
                    } else {
                        return false;
                    }
                } else {
                    String msg = "Value for required attribute " + key + " found empty.";
                    log.error(msg);
                    throw new GovernanceException(msg);
                }
            }
        }
        return true;
    }

    /**
     * Method to convert the expression specified for storing the path with corresponding values
     * where the artifact is stored. This method will return multiple paths.
     *
     * @param pathExpression the expression specified for storing the path
     * @param artifact       the governance artifact
     *
     * @return               the paths with corresponding values where the artifact is stored
     *
     * @throws GovernanceException if the operation failed.
     */
    public static String[] getPathsFromPathExpression(String pathExpression,
                                                   GovernanceArtifact artifact)
            throws GovernanceException {
        String expression = replaceNameAndNamespace(pathExpression, artifact);
        String[] elements = expression.split("@");
        for (int i = 1; i < elements.length; i++) {
            if (!(elements[i].indexOf(":") > 0) &&
                    elements[i].indexOf("}") > 0 && elements[i].indexOf("{") == 0) {
                String key = elements[i].split("}")[0].substring(1);
                String artifactAttribute = artifact.getAttribute(key);
                if (artifactAttribute != null) {
                    expression = expression.replace("@{" + key + "}", artifactAttribute);
                }
            }
        }
        List<String> output = fixExpressionForMultiplePaths(artifact, expression);
        return output.toArray(new String[output.size()]);
    }

    private static List<String> fixExpressionForMultiplePaths(GovernanceArtifact artifact,
                                                          String expression)
            throws GovernanceException {
        if (expression.indexOf("@") < 0) {
            return Collections.singletonList(expression);
        }
        List<String> output = new LinkedList<String>();
        String[] elements = expression.split("@");
        for (int i = 1; i < elements.length; i++) {
            if (elements[i].indexOf("}") > 0 && elements[i].indexOf("{") == 0) {
                String key = elements[i].split("}")[0].substring(1).split(":")[0];
                String[] artifactAttributes = artifact.getAttributes(key);
                if (artifactAttributes != null) {
                    for (String artifactAttribute : artifactAttributes) {
                        String[] parts = artifactAttribute.split(":");
                        output.addAll(fixExpressionForMultiplePaths(artifact,
                                expression.replace("@{" + key + ":key}", parts[0]).replace(
                                        "@{" + key + ":value}", parts[1])));
                    }
                }
                break;
            }
        }
        return output;
    }

    private static String replaceNameAndNamespace(String pathExpression,
                                                  GovernanceArtifact artifact) {
        String output = pathExpression;
        QName qName = artifact.getQName();

        if (qName != null) {
            output = output.replace("@{name}", qName.getLocalPart());
            String replacement =
                    CommonUtil.derivePathFragmentFromNamespace(qName.getNamespaceURI());
            if (replacement.startsWith("/")) {
                replacement = replacement.substring(1);
            }
            if (replacement.endsWith("/")) {
                replacement = replacement.substring(0, replacement.length() - 1);
            }
            output = output.replace("@{namespace}", replacement);
        }
        return output;
    }

    /**
     * Method to obtain all available aspects for the given tenant.
     *
     * @return list of available aspects.
     *
     * @throws RegistryException if the operation failed.
     */
    public static String[] getAvailableAspects() throws RegistryException {
        int tenantId = CarbonContext.getCurrentContext().getTenantId();
        Registry systemRegistry = registryService.getConfigSystemRegistry(tenantId);
        String[] aspectsToAdd = systemRegistry.getAvailableAspects();
        if (aspectsToAdd == null) {
            return new String[0];
        }
        List<String> lifecycleAspectsToAdd = new LinkedList<String>();
        boolean isTransactionStarted = false;
        String tempResourcePath = "/governance/lcm/" + UUIDGenerator.generateUUID();
        for (String aspectToAdd : aspectsToAdd) {
            if (systemRegistry.getRegistryContext().isReadOnly()) {
                lifecycleAspectsToAdd.add(aspectToAdd);
                continue;
            }
            Map<String, Boolean> aspectsMap;
            if (!lifecycleAspects.containsKey(tenantId)) {
                synchronized(ASPECT_MAP_LOCK) {
                    if (!lifecycleAspects.containsKey(tenantId)) {
                        aspectsMap = new HashMap<String, Boolean>();
                        lifecycleAspects.put(tenantId, aspectsMap);
                    } else {
                        aspectsMap = lifecycleAspects.get(tenantId);
                    }
                }
            } else {
                aspectsMap = lifecycleAspects.get(tenantId);
            }
            Boolean isLifecycleAspect = aspectsMap.get(aspectToAdd);
            if (isLifecycleAspect == null) {
                if (!isTransactionStarted) {
                    systemRegistry.beginTransaction();
                    isTransactionStarted = true;
                }
                systemRegistry.put(tempResourcePath, systemRegistry.newResource());
                systemRegistry.associateAspect(tempResourcePath, aspectToAdd);
                Resource r = systemRegistry.get(tempResourcePath);
                Properties props = r.getProperties();
                Set keys = props.keySet();
                for (Object key : keys) {
                    String propKey = (String) key;
                    if (propKey.startsWith("registry.lifecycle.")
                            || propKey.startsWith("registry.custom_lifecycle.checklist.")) {
                        isLifecycleAspect = Boolean.TRUE;
                        break;
                    }
                }
                if (isLifecycleAspect == null) {
                    isLifecycleAspect = Boolean.FALSE;
                }
                aspectsMap.put(aspectToAdd, isLifecycleAspect);
            }
            if (isLifecycleAspect) {
                lifecycleAspectsToAdd.add(aspectToAdd);
            }
        }
        if (isTransactionStarted) {
            systemRegistry.delete(tempResourcePath);
            systemRegistry.rollbackTransaction();
        }
        return lifecycleAspectsToAdd.toArray(new String[lifecycleAspectsToAdd.size()]);
    }

    /**
     * Method to obtain a path from a qualified name.
     *
     * @param qName the qualified name.
     * @return the corresponding path.
     */
    public static String derivePathFromQName(QName qName) {
        String serviceName = qName.getLocalPart();
        String serviceNamespace = qName.getNamespaceURI();
        return (serviceNamespace == null ?
                "" : CommonUtil.derivePathFragmentFromNamespace(serviceNamespace)) + serviceName;
    }

    /**
     * Obtain a name that can represent a URL.
     *
     * @param url the URL.
     *
     * @return the name.
     */
    public static String getNameFromUrl(String url) {
        int slashIndex = url.lastIndexOf('/');
        if (slashIndex == -1) {
            return url;
        }
        if (slashIndex == url.length() - 1) {
            return url.substring(0, url.length() - 1);
        }
        return url.substring(slashIndex + 1);
    }

    @SuppressWarnings("unchecked")
    public static List<OMElement> evaluateXPathToElements(String expression,
                                                          OMElement root) throws JaxenException {

        String[] wsdlPrefixes = {
                "wsdl", "http://schemas.xmlsoap.org/wsdl/",
                "wsdl2", "http://www.w3.org/ns/wsdl",
                "xsd", "http://www.w3.org/2001/XMLSchema",
                "soap", "http://schemas.xmlsoap.org/wsdl/soap/",
                "soap12", "http://schemas.xmlsoap.org/wsdl/soap12/",
                "http", "http://schemas.xmlsoap.org/wsdl/http/",
        };
        AXIOMXPath xpathExpression = new AXIOMXPath(expression);

        for (int j = 0; j < wsdlPrefixes.length; j++) {
            xpathExpression.addNamespace(wsdlPrefixes[j++], wsdlPrefixes[j]);
        }
        return (List<OMElement>) xpathExpression.selectNodes(root);
    }

    /**
     * Method to associate an aspect with a given resource on the registry.
     *
     * @param path     the path of the resource.
     * @param aspect   the aspect to add.
     * @param registry the registry instance on which the resource is available.
     *
     * @throws RegistryException if the operation failed.
     */
    public static void associateAspect(String path, String aspect, Registry registry)
            throws RegistryException {

        try {
            registry.associateAspect(path, aspect);

        } catch (RegistryException e) {

            String msg = "Failed to associate aspect with the resource " +
                    path + ". " + e.getMessage();
            log.error(msg, e);
            throw new RegistryException(msg, e);
        }
    }

    /**
     * Method to remove an aspect from a given resource on the registry.
     *
     * @param path     the path of the resource.
     * @param aspect   the aspect to be removed.
     * @param registry the registry instance on which the resource is available.
     *
     * @throws RegistryException if the operation failed.
     */
    public static void removeAspect(String path, String aspect, Registry registry)
            throws RegistryException {

        try {
            /* set all the variables to the resource */
            Resource resource = registry.get(path);
            Properties props = resource.getProperties();
            //List<Property> propList = new ArrayList<Property>();
            Iterator iKeys = props.keySet().iterator();
            ArrayList<String> propertiesToRemove = new ArrayList<String>();

            while (iKeys.hasNext()) {
                String propKey = (String) iKeys.next();

                if (propKey.startsWith("registry.custom_lifecycle.votes.")
                    ||propKey.startsWith("registry.custom_lifecycle.user.")
                    ||propKey.startsWith("registry.custom_lifecycle.checklist.")
                    || propKey.startsWith("registry.LC.name")
                    || propKey.startsWith("registry.lifecycle.")
                    || propKey.startsWith("registry.Aspects")) {
                    propertiesToRemove.add(propKey);
                }
            }

            for(String propertyName : propertiesToRemove) {
                resource.removeProperty(propertyName);
            }

            registry.put(path, resource);

        } catch (RegistryException e) {

            String msg = "Failed to remove aspect " + aspect +
                    " on resource " + path + ". " + e.getMessage();
            log.error(msg, e);
            throw new RegistryException(msg, e);
        }
    }

    public static AttributeSearchService getAttributeSearchService() {
        return attributeSearchService;
    }

    public static void setAttributeSearchService(AttributeSearchService attributeSearchService) {
        GovernanceUtils.attributeSearchService = attributeSearchService;
    }

    /**
     * Returns a list of governance artifacts found by searching indexes. This method requires an instance of an
     * attribute search service.
     *
     * @param criteria the search criteria
     * @param registry the governance registry instance
     *
     * @return search result
     *
     * @throws GovernanceException if the operation failed
     */
    public static List<GovernanceArtifact> findGovernanceArtifacts(
            Map<String, List<String>> criteria, Registry registry, String mediaType) throws GovernanceException {
        if (getAttributeSearchService() == null) {
            throw new GovernanceException("Attribute Search Service not Found");
        }
        List<GovernanceArtifact> artifacts = new ArrayList<GovernanceArtifact>();
        Map<String, String> fields = new HashMap<String, String>();
        if (mediaType != null) {
            fields.put("mediaType", mediaType);
        }
        for (Map.Entry<String, List<String>> e : criteria.entrySet()) {
            StringBuilder builder = new StringBuilder();
            for (String referenceValue : e.getValue()) {
                if (referenceValue != null && !"".equals(referenceValue)) {
                    builder.append(referenceValue).append(",");
                }
            }
            if (builder.length() > 0) {
                fields.put(e.getKey(), builder.substring(0, builder.length() - 1));
            }
        }

            try {
                ResourceData[] results = getAttributeSearchService().search(fields);
                for (ResourceData result : results) {
                GovernanceArtifact governanceArtifact = retrieveGovernanceArtifactByPath(
                            registry,
                            result.getResourcePath().substring(RegistryConstants.GOVERNANCE_REGISTRY_BASE_PATH.length()));
                    if(governanceArtifact!=null){
                        artifacts.add(governanceArtifact);
                    }
                }
            } catch (RegistryException e) {
                throw new GovernanceException("Unable to search by attribute", e);

        }
        return artifacts;
    }

    /*
     * This method is used to retrieve departments attached to a given artifact. Applicable to
     * ProjectGroup and Person artifacts
     *
     * @param registry - Registry associated with <code>artifact</code>
     * @param artifact - ProjectGroup or Person artifact to which Departments are attached
     * @return Department artifacts attached to <code>artifact</code>
     * @throws GovernanceException If operation fails
     */
   /* public static Department[] getAffiliatedDepartments(Registry registry, PeopleArtifact artifact)
                                        throws GovernanceException {
        List<Department> list = new ArrayList<Department>();
        PeopleManager manager = new PeopleManager(registry);
        String[] affiliations = artifact.getAttributes(GovernanceConstants.AFFILIATIONS_ATTRIBUTE);

        if (affiliations != null) {
            for (String deptText : affiliations) {
                String deptName = deptText.split(GovernanceConstants.ENTRY_VALUE_SEPARATOR)[1];
                *//* We are assuming data consistency at this point and hence, not checking the 0th
                   element of the above returned array *//*
                PeopleArtifact pa = manager.getPeopleArtifactByName(deptName);
                if (pa instanceof Department) {
                    list.add((Department) pa);
                }
            }
        }
        return list.toArray(new Department[list.size()]);
    }*/

    /*
     * This method is used to retrieve organizations attached to a given artifact. Applicable to
     * ProjectGroup and Person artifacts
     *
     * @param registry - Registry associated with <code>artifact</code>
     * @param artifact - ProjectGroup or Person artifact to which Organizations are attached
     * @return Organization artifacts attached to <code>artifact</code>
     * @throws GovernanceException If operation fails
     */
    /*public static Organization[] getAffiliatedOrganizations(Registry registry,
                                                            PeopleArtifact artifact)
                                        throws GovernanceException {
        List<Organization> list = new ArrayList<Organization>();
        PeopleManager manager = new PeopleManager(registry);

        String[] affiliations = artifact.getAttributes(GovernanceConstants.AFFILIATIONS_ATTRIBUTE);
        if (affiliations != null) {
            for (String orgText : affiliations) {
                String orgName = orgText.split(GovernanceConstants.ENTRY_VALUE_SEPARATOR)[1];
                *//* We are assuming data consistency at this point and hence, not checking the 0th
                   element of the above returned array *//*
                PeopleArtifact pa = manager.getPeopleArtifactByName(orgName);
                if (pa instanceof Organization) {
                    list.add((Organization) pa);
                }
            }
        }
        return list.toArray(new Organization[list.size()]);
    }*/

    /*
     * This method is used to retrieve project groups attached to a given artifact. Applicable to
     * Person artifacts
     *
     * @param registry - Registry associated with <code>artifact</code>
     * @param artifact - Person artifact to which project groups are attached
     * @return ProjectGroup artifacts attached to <code>artifact</code>
     * @throws GovernanceException If operation fails
     */
    /*public static ProjectGroup[] getAffiliatedProjectGroups(Registry registry,
                                                            PeopleArtifact artifact)
                                        throws GovernanceException {
        List<ProjectGroup> list = new ArrayList<ProjectGroup>();
        PeopleManager manager = new PeopleManager(registry);
        String[] affiliations = artifact.getAttributes(GovernanceConstants.AFFILIATIONS_ATTRIBUTE);
        if (affiliations != null) {
            for (String pgText : affiliations) {
                String pgName = pgText.split(GovernanceConstants.ENTRY_VALUE_SEPARATOR)[1];
                *//* We are assuming data consistency at this point and hence, not checking the 0th
          element of the above returned array *//*
                PeopleArtifact pa = manager.getPeopleArtifactByName(pgName);
                if (pa instanceof ProjectGroup) {
                    list.add((ProjectGroup) pa);
                }
            }
        }
        return list.toArray(new ProjectGroup[list.size()]);
    }*/

    /*
     * This method is used to retrieve project groups that have the given artifact (Organization or
     * Department) as an affiliation
     *
     * @param registry - Registry associated with <code>artifact</code>
     * @param artifact - Organization/Department artifact
     * @return ProjectGroups that have <code>artifact</code> as an affiliation
     * @throws GovernanceException
     */
/*
    public static ProjectGroup[] getAttachedProjectGroups(Registry registry, PeopleArtifact artifact)
                                               throws GovernanceException {
        ProjectGroup[] pgs = new PeopleManager(registry).getAllProjectGroups();
        List<ProjectGroup> list = new ArrayList<ProjectGroup>();
        for (ProjectGroup pg : pgs) {
            for (Department department : pg.getDepartments()) {
                if (artifact.getName().equals(department.getName())) {
                    list.add(pg);
                }
            }
        }
        return list.toArray(new ProjectGroup[list.size()]);
    }
*/

    /*
     * This method is used to retrieve persons that have the given artifact (Organization or
     * Department) as an affiliation
     *
     * @param registry - Registry associated with <code>artifact</code>
     * @param artifact - Organization/Department artifact
     * @return Person artifacts that have <code>artifact</code> as an affiliation
     * @throws GovernanceException
     */
/*
    public static Person[] getAttachedPersons(Registry registry, PeopleArtifact artifact)
                                      throws GovernanceException {
        Person[] persons = new PeopleManager(registry).getAllPersons();
        List<Person> list = new ArrayList<Person>();
        for (Person person : persons) {
            for (Department department : person.getDepartments()) {
                if (artifact.getName().equals(department.getName())) {
                    list.add(person);
                }
            }
        }
        return list.toArray(new Person[list.size()]);
    }
*/

    /*
     * This method writes sub-group associations contained within the given ProjectGroup to the
     * registry. Existence of all the sub groups must be validated before calling this method.
     *
     * @param registry
     * @param projectGroup
     * @throws GovernanceException
     */
/*
    public static void writeSubGroupAssociations(Registry registry, ProjectGroup projectGroup)
                                          throws GovernanceException {
        try {
            if (!registry.resourceExists(projectGroup.getPath())) {
                return;
            }
            ProjectGroup[] subGroups = projectGroup.getSubGroups();
            // Get the existing association list which is related to the current operation
            Set<String> existingSet = new HashSet<String>();
            for (Association asso : registry.getAllAssociations(projectGroup.getPath())) {
                if ((GovernanceConstants.SUB_GROUP.equals(asso.getAssociationType()) &&
                        asso.getSourcePath().equals(projectGroup.getPath()))
                        ||
                        (GovernanceConstants.IS_PART_OF.equals(asso.getAssociationType()) &&
                                asso.getDestinationPath().equals(projectGroup.getPath()))) {
                    existingSet.add(asso.getSourcePath() + SEPARATOR + asso.getDestinationPath() +
                            SEPARATOR + asso.getAssociationType());
                }
            }

            // Get the updated association list from the projectGroup object
            Set<String> updatedSet = new HashSet<String>();
            for (ProjectGroup subGroup : subGroups) {
                updatedSet.add(projectGroup.getPath() + SEPARATOR + subGroup.getPath() +
                        SEPARATOR + GovernanceConstants.SUB_GROUP);
                updatedSet.add(subGroup.getPath() + SEPARATOR + projectGroup.getPath() + SEPARATOR +
                        GovernanceConstants.IS_PART_OF);
            }
            updateAssociations(registry, existingSet, updatedSet);
        } catch (RegistryException e) {
            String msg = "Error in writing sub group associations, parent project-group id: " +
                    projectGroup.getId() + ", path: " + projectGroup.getPath();
            log.error(msg, e);
            throw new GovernanceException(msg, e);
        }
    }
*/

    /*
     * This method writes owner associations contained within the service object to the registry.
     * Existence of all the owners as people artifacts must be validated before calling this method.
     *
     * @param registry
     * @param service
     * @throws GovernanceException
     */
/*
    public static void writeOwnerAssociations(Registry registry, Service service)
                                       throws GovernanceException {
        try {
            if (!registry.resourceExists(service.getPath())) {
                return;
            }
            PeopleArtifact[] owners = service.getOwners();
            // Remove associations that are not there anymore and add any new associations
            Association[] oldAssociations = registry.getAllAssociations(service.getPath());
            Set<String> oldSet = new HashSet<String>();
            for (Association association : oldAssociations) {
                if (GovernanceConstants.OWNED_BY.equals(association.getAssociationType()) ||
                        GovernanceConstants.OWNS.equals(association.getAssociationType())) {
                    oldSet.add(association.getSourcePath() + SEPARATOR +
                            association.getDestinationPath() + SEPARATOR +
                            association.getAssociationType());
                }
            }
            Set<String> updatedSet = new HashSet<String>();
            for (PeopleArtifact owner : owners) {
                updatedSet.add(service.getPath() + SEPARATOR + owner.getPath() + SEPARATOR +
                        GovernanceConstants.OWNED_BY);
                updatedSet.add(owner.getPath() + SEPARATOR + service.getPath() + SEPARATOR +
                        GovernanceConstants.OWNS);
            }
            updateAssociations(registry, oldSet, updatedSet);
        } catch (RegistryException e) {
            String msg = "Error in associating owners to service. Id: " + service.getId() +
                    ", path: " + service.getPath();
            log.error(msg, e);
            throw new GovernanceException(msg, e);
        }
    }
*/

    /*
     * This method writes consumer associations contained within the service object to the registry.
     * Existence of all the consumers as people artifacts must be validated before calling this
     * method.
     *
     * @param registry
     * @param service
     * @throws GovernanceException
     */
/*
    public static void writeConsumerAssociations(Registry registry, Service service)
                                         throws GovernanceException {
        try {
            if (!registry.resourceExists(service.getPath())) {
                return;
            }
            PeopleArtifact[] consumers = service.getConsumers();
            // Remove associations that are not there anymore and add any new associations
            Association[] oldAssociations = registry.getAllAssociations(service.getPath());
            Set<String> oldSet = new HashSet<String>();
            for (Association association : oldAssociations) {
                if (GovernanceConstants.CONSUMED_BY.equals(association.getAssociationType()) ||
                        GovernanceConstants.CONSUMES.equals(association.getAssociationType())) {
                    oldSet.add(association.getSourcePath() + SEPARATOR +
                            association.getDestinationPath() + SEPARATOR +
                            association.getAssociationType());
                }
            }
            Set<String> updatedSet = new HashSet<String>();
            for (PeopleArtifact consumer : consumers) {
                updatedSet.add(service.getPath() + SEPARATOR + consumer.getPath() + SEPARATOR +
                        GovernanceConstants.CONSUMED_BY);
                updatedSet.add(consumer.getPath() + SEPARATOR + service.getPath() + SEPARATOR +
                        GovernanceConstants.CONSUMES);
            }
            updateAssociations(registry, oldSet, updatedSet);
        } catch (RegistryException e) {
            String msg = "Error in associating owners to service. Id: " + service.getId() +
                    ", path: " + service.getPath();
            log.error(msg, e);
            throw new GovernanceException(msg, e);
        }
    }
*/

    /*
     * This method extracts people names from the given attribute of the given artifact and returns
     * an array containing PeopleArtifacts represented by those names.
     * Existence of people artifacts listed under the atrribute name must be validated before
     * calling this method.
     *
     * @param registry      Associated registry
     * @param artifact      GovernanceArtifact which stores people list as an attribute
     * @param attributeName Name of the attribute which stores people names
     * @throws GovernanceException
     */
/*
    public static PeopleArtifact[] extractPeopleFromAttribute(Registry registry,
                                                              GovernanceArtifact artifact,
                                                              String attributeName)
                                                throws GovernanceException {
        String[] peopleTexts = artifact.getAttributes(attributeName);
        PeopleManager manager = new PeopleManager(registry);
        List<PeopleArtifact> list = new ArrayList<PeopleArtifact>();
        if (peopleTexts != null) {
            for (String peopleText : peopleTexts) {
                String name = peopleText.split(GovernanceConstants.ENTRY_VALUE_SEPARATOR)[1];
                PeopleArtifact pa = manager.getPeopleArtifactByName(name);
                if (pa == null) {
                    String msg = "Invalid people artifact name is found within the governance " +
                            "artifact. Path: " + artifact.getPath() + ", Invalid people artifact " +
                            "name:" + name;
                    log.error(msg);
                    throw new GovernanceException(msg);
                } else {
                    list.add(pa);
                }
            }
        }
        return list.toArray(new PeopleArtifact[list.size()]);
    }
*/

/*
    private static void updateAssociations(Registry registry, Set<String> existingAssociationSet,
                                           Set<String> updatedAssociationSet)
                                    throws RegistryException {
        Set<String> removedAssociations = new HashSet<String>(existingAssociationSet);
        removedAssociations.removeAll(updatedAssociationSet);

        Set<String> newAssociations = new HashSet<String>(updatedAssociationSet);
        newAssociations.removeAll(existingAssociationSet);

        for (String removedAssociation : removedAssociations) {
            String[] params = removedAssociation.split(SEPARATOR);
            try {
                for (int i = 0; i < 2; i++) {
                    if (GovernanceUtils.retrieveGovernanceArtifactByPath(registry, params[i])
                            instanceof PeopleArtifact) {
                        registry.removeAssociation(params[0], params[1], params[2]);
                        break;
                    }
                }
            } catch (GovernanceException ignored) {
            }
        }

        for (String newAssociation : newAssociations) {
            String[] params = newAssociation.split(SEPARATOR);
            registry.addAssociation(params[0], params[1], params[2]);
        }
    }
*/
}