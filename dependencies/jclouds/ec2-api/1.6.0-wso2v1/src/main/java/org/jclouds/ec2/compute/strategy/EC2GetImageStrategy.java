/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.ec2.compute.strategy;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.getOnlyElement;

import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.aws.util.AWSUtils;
import org.jclouds.compute.strategy.GetImageStrategy;
import org.jclouds.ec2.EC2Client;
import org.jclouds.ec2.domain.Image;
import org.jclouds.ec2.options.DescribeImagesOptions;

import com.google.common.base.Function;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class EC2GetImageStrategy implements GetImageStrategy {

   private final EC2Client client;
   private final Function<Image, org.jclouds.compute.domain.Image> imageToImage;

   @Inject
   protected EC2GetImageStrategy(EC2Client client, Function<Image, org.jclouds.compute.domain.Image> imageToImage) {
      this.client = checkNotNull(client, "client");
      this.imageToImage = checkNotNull(imageToImage, "imageToImage");
   }

   @Override
   public org.jclouds.compute.domain.Image getImage(String id) {
      checkNotNull(id, "id");
      String[] parts = AWSUtils.parseHandle(id);
      String region = parts[0];
      String instanceId = parts[1];
      try {
         Image image = getImageInRegion(region, instanceId);
         return imageToImage.apply(image);
      } catch (NoSuchElementException e) {
         return null;
      }
   }

   public Image getImageInRegion(String region, String id) {
      return getOnlyElement(client.getAMIServices().describeImagesInRegion(region,
               DescribeImagesOptions.Builder.imageIds(id)));
   }

}
