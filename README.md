# Pixpie Android SDK

https://www.pixpie.co
[![Analytics](https://ga-beacon.appspot.com/UA-88187046-1/pixpie-github/AndroidSDK?pixel)](https://github.com/PixpieCo/AndroidSDK)

## What is it for? ##

Pixpie is a Platform as a Service for image optimization and manipulation.

Android SDK provides API methods implementation to access Pixpie REST API with additional features: 
- automatic image adaptation for image view components based on current device parameters
- measuring of network quality
- usage of built-in cache
- easy integration with other libraries like [Picasso](https://github.com/square/picasso) and [Glide](https://github.com/bumptech/glide)

## How to start? ##

Check [Getting started](https://pixpie.atlassian.net/wiki/display/DOC/Getting+started) guide and [register](https://cloud.pixpie.co/registration) your account

## Add dependency ##

Check last released version on [Bintray](https://dl.bintray.com/accord/pixpie-android/co/pixpie/api/android/pixpie-android/).

Update your gradle file:

``` gradle
repositories {
    ...
    maven {
        url  "https://dl.bintray.com/accord/pixpie-android"
    }
    ...
}

dependencies {
    ...
    compile 'co.pixpie.api.android:pixpie-android:{VERSION}'
    ...
}
```

Another way is add source code as the module in project.

### Authentication ###

After [creation of new application](https://pixpie.atlassian.net/wiki/display/DOC/Create+application),
use Bundle ID (reverse url id) and Secret key in static authenticate method.
Authentication is processing in separate thread and is non-blocking operation.

``` java

  PixpieApi.authenticate(context, // application context, required
      new PixpieConfiguration()
          .withReverseUrlId(“com.example.SomeApp”) // Bundle ID (reverse url id), required
          .withSecretKey("41bc32fde0d3ed6927b6f54sdc") // Secret key, required
          .withSalt("yuuRiesahs3niet7thac") // fixed value for cloud usage, required
          .withUserId("some-unique-user-id") // Unique User ID (for A/B testing and extended statistic), optional
          .withDeviceId("some-unique-device-id") // Unique Device ID (for extended statistic), optional
          .withDeviceDescription("some-device-description") // Current device description(for extended statistic), optional
          .withCacheEntriesNumber(400)); // Max cache entries in case of built-in cache usage, default - 200, optional
``` 

### Get remote (third party) images ###

Approaches how to manipulate and optimize images that are available by the direct link.

##### Using built-in cache and network #####

As a result of method execution the optimized image will be shown in ImageView instance.

``` java

  String imageUrl = "http://i.imgur.com/ByDKcYg.jpg";

  PixpieApi.with(context).getRemoteImage(String imageUrl, ImageView view,
      new ImageTransformation().withHeight(300)....withQuality(85)...,
      Response.Listener<PixpieResult> listener ) // optional     

``` 

Width, height and quality could be set forcibly in ImageTransformation class. If not:
- width and height are set automatically based on ImageView parameters
- quality value is generated based on current network type (default - 75%)
- requested from server image format by default is webp, it can be changed to original (JPEG, PNG)

##### Using custom cache and network #####

In case when application already using some image networking library, it's easy to generate url to get optimized image from Pixpie cloud.

Glide example:

``` java

  String imageUrl = "http://i.imgur.com/ByDKcYg.jpg";

  String pixpieImageUrl = PixpieApi.with(context).getRemoteImageUrl(String imageUrl, 
    new ImageTransformation().withHeight(300)....withQuality(85)...,
    ImageUrlListener listener ) // optional
  
  Glide.with(MainActivity.this).load(pixpieImageUrl)into(imageView);
    
```     

### Upload ###

There are a few ways how to upload local images to Pixpie cloud.
- through [Web panel](https://pixpie.atlassian.net/wiki/display/DOC/Upload+image)
- using [REST API](https://pixpie.atlassian.net/wiki/display/DOC/Upload)
- using [SDKs](https://pixpie.atlassian.net/wiki/display/DOC/Client+and+server+SDKs)

``` java

  PixpieApi.with(context).uploadImage(@NonNull byte[] image, @NonNull String contentType,
                                      @NonNull String encodedImageName, @NonNull String innerPath,
                                      Response.Listener<PixpieResult> listener)

```

- image - binary data
- contentType -  “image/png" or “image/jpeg”
- encodedImageName - image name in Pixpie
- innerPath - relative inner path where it will be saved in Pixpie cloud

### Get local (uploaded) images ###

Behaviour of methods that provide possibility to show or get the urls of uploaded images is very similar to remote (third party images).

``` java

  String relativePathToImageInPixpieCloud = “/path/to/image/some-image-1.jpg”

  PixpieApi.with(context).getImage(String relativePathToImageInPixpieCloud, ImageView view,
    new ImageTransformation().withWidth(200)....withCropAlign(CropAlignType.TOP)...)
    Response.Listener<PixpieResult> listener ) // optional 

```

To get the link:

``` java

  String relativePathToImageInPixpieCloud = “/path/to/image/some-image-1.jpg”

  String pixpieUrl = PixpieApi.with(context).getImageUrl(String relativePathToImageInPixpieCloud, 
    new ImageTransformation().withHeight(300)...withQuality(80)....withCropAlign(CropAlignType.BOTTOM_LEFT)...)
  
  // show the image by pixpieUrl using Glide, Picasso etc.
  
```

### Delete ###

Uploaded local images could be simply deleted from Pixpie cloud by calling the delete method.


``` java

  PixpieApi.with(context).deleteImage(@NonNull String imageName, 
    @NonNull String pathToImage, Response.Listener<PixpieResult> listener)

```    

## License

Pixpie Android SDK is available under the Apache 2.0 license.

    Copyright (C) 2015,2016 Pixpie

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

