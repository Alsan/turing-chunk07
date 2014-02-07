/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/

package org.wso2.carbon.am.tests.sample;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.am.tests.APIManagerIntegrationTest;
import org.wso2.carbon.am.tests.util.APILifeCycleState;
import org.wso2.carbon.am.tests.util.APIPublisherRestClient;
import org.wso2.carbon.am.tests.util.APIStoreRestClient;
import org.wso2.carbon.am.tests.util.bean.AddAPIRequest;
import org.wso2.carbon.am.tests.util.bean.AddAPISubscriptionRequest;
import org.wso2.carbon.am.tests.util.bean.GenerateAppKeyRequest;
import org.wso2.carbon.am.tests.util.bean.UpdateAPILifeCycleStateRequest;
import org.wso2.carbon.automation.core.utils.HttpRequestUtil;
import org.wso2.carbon.automation.core.utils.HttpResponse;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class YouTubeAPITestCase extends APIManagerIntegrationTest {
    private APIPublisherRestClient apiPublisher;
    private APIStoreRestClient apiStore;

    @BeforeClass(alwaysRun = true)
    public void init() throws Exception {
        super.init(0);
        apiPublisher = new APIPublisherRestClient(getServerURLHttp());
        apiStore = new APIStoreRestClient(getServerURLHttp());
    }

    @Test(groups = {"wso2.am"}, description = "You Tube sample")
    public void testYouTubeApiSample() throws Exception {
        apiPublisher.login(userInfo.getUserName(), userInfo.getPassword());
        AddAPIRequest apiRequest = new AddAPIRequest("YoutubeFeeds", "youtube", new URL("http://gdata.youtube.com/feeds/api/standardfeeds"));
        apiPublisher.addAPI(apiRequest);

        UpdateAPILifeCycleStateRequest updateRequest = new UpdateAPILifeCycleStateRequest("YoutubeFeeds", userInfo.getUserName(), APILifeCycleState.PUBLISHED);
        apiPublisher.changeAPILifeCycleStatusTo(updateRequest);

        apiStore.login(userInfo.getUserName(), userInfo.getPassword());
        AddAPISubscriptionRequest subscriptionRequest = new AddAPISubscriptionRequest("YoutubeFeeds", userInfo.getUserName());
        apiStore.subscribe(subscriptionRequest);

        GenerateAppKeyRequest generateAppKeyRequest = new GenerateAppKeyRequest("DefaultApplication");
        String responseString = apiStore.generateApplicationKey(generateAppKeyRequest).getData();
        JSONObject response = new JSONObject(responseString);
        String accessToken = response.getJSONObject("data").getJSONObject("key").get("accessToken").toString();
        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("Authorization", "Bearer " + accessToken);

        Thread.sleep(2000);
        HttpResponse youTubeResponse = HttpRequestUtil.doGet(getApiInvocationURLHttp("youtube/1.0.0/most_popular"), requestHeaders);
        Assert.assertEquals(youTubeResponse.getResponseCode(), 200, "Response code mismatched when api invocation");
        Assert.assertTrue(youTubeResponse.getData().contains("<feed"), "Response data mismatched when api invocation");
        Assert.assertTrue(youTubeResponse.getData().contains("<category"), "Response data mismatched when api invocation");
        Assert.assertTrue(youTubeResponse.getData().contains("<entry>"), "Response data mismatched when api invocation");

    }

    @AfterClass(alwaysRun = true)
    public void destroy() throws Exception {
        super.cleanup();
    }
}