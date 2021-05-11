# Custom Checkout Step to ask for documents
![Freelancer](additional-info-checkout-step.png
)

This module adds a new checkout step with an "Upload File" button so the customer can attach additional information to the order.
There is no business logic behind this step, it's just to show how easy is to extend the checkout process using Liferay Commerce extension points.


Developed to run on the following versions of Liferay and/or Commerce: `Liferay DXP 7.3-GA1` or `Liferay DXP 7.3-SP1`with `Commerce 3.0`


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

### To Download the compiled JAR

You can download the JAR from the releases section


