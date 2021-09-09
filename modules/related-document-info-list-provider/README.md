# Custom Collection Provider for Documents of my Purchased Products

This module adds a new collection provider (InfoLIstProvider) to use it in an Asset Publisher widget or with the Collection Display fragment.
This collection provider queries the database looking for the unique documents that are set as attachments of products purchased by a given commerce account.
This Collection Provider use a custom service that provides the list of purchased products by an account. This custom service is created in its own OSGi modules (api and imp) and therefore is a dependency for the Collection Provider module


Developed to run on the following versions of Liferay and/or Commerce: `Liferay DXP 7.3 - FP1` with `Commerce 3.0`


## How to Build and Deploy to Liferay

You will need to build and deploy 3 modules:

* related-document-info-list-provider
* [commerce-custom-service-api](https://github.com/danieldefrancisco-lr/liferay-73-commerce-30-workspace/tree/main/modules/commerce-custom-service-api)
* [commerce-custom-service](https://github.com/danieldefrancisco-lr/liferay-73-commerce-30-workspace/tree/main/modules/commerce-custom-service)

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

