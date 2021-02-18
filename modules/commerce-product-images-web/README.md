# Product Images widget for the product detail page

This module adds a new portlet to be used in a product details page, together with the Product Details widget
This widget takes the product images and display them in a carousel. You can place this widget anywhere you want in the page as long as the Product Details widget is also included in the page
It is inspired by Jeff Handa's Carousel Renderer demo asset
[Jeff Handa's Carousel Renderer demo asset](https://sales.liferay.com/web/library/w/carousel-renderer?redirect=%2Fgroup%2Flibrary%2Fdemo-assets%2F-%2Fcategories%2F234726%3Fp_r_p_resetCur%3Dtrue%26p_r_p_categoryId%3D234726)

You may want to improve the styling of the carouse if you want :)

Developed to run on the following versions of Liferay and/or Commerce: `Liferay DXP 7.3` with `Commerce 3.0`


## How to Build and Deploy to Liferay

Follow the steps below to build and deploy or copy the modules from the [releases](../../releases/latest) page to your Liferay's deploy folder.

In order to build or deploy any of these modules you will need to either import this workspace into your IDE and use the gradle plugins of the IDE or  [install Blade CLI](https://help.liferay.com/hc/en-us/articles/360028833852-Installing-Blade-CLI).

### To Build

You can either import this Liferay Workspace into your IDE and use the gradle plugin of the IDE, or use any of the CLI commands:

`$ gradlew build`
or
`$ blade gw build`

You can find the built modules at `modules/{module-name}/build/libs/{module-name}.jar`.

### To Deploy

In `gradle-local.properties` add the following line to point towards the Liferay instance you want to deploy to:
```
liferay.workspace.home.dir=/path/to/liferay/home
```
and then execute the "deploy" task of gradle, either from the IDE or using the CLI:

`$ gradlew deploy`
or
`$ blade gw deploy`

Alternatively, you can always copy the JAR files and paste them in the `{liferay-bundle}/deploy` folder.

