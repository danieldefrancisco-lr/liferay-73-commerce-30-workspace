# ${Product Publisher custom Datasource for most frequently wishlisted products}

This module adds a new datasource for the Product Publisher widget.
This datasource queries the database looking for the products with a higher frecuency in all wislishts.
A.K.A: The 5 most popular products of your catalog based on how many times they were added to a wishlist by a user. 
It takes the 5 most frequent products in all wishlists.

To change that number, you need to edit the `/modules/commerce-demo-realestate-datasource/src/main/java/commerce/demo/realestate/datasource/CommerceDemoRealestateWishlistDatasourceConstants.java`

Future versions will provide a Settings entry to change this number.

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

