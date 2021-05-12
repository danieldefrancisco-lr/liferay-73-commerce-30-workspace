# Product Publisher custom Datasource for My Retrofitting Products

## What is retrofitting
Retrofitting describes the measures taken in the manufacturing industry to allow new or updated parts to be fitted to old or outdated assemblies (like blades to wind turbines).

Retrofitting parts are necessary for manufacturers when the design of a large assembly is changed or revised. If, after the changes have been implemented, a customer (with an old version of the product) wishes to purchase a replacement part, then retrofit parts and assembling techniques will have to be used so that the revised parts will fit suitably onto the older assembly.

## Datasource description
This module adds a new datasource for the Product Publisher widget.
This datasource queries the database looking for the retrofitting parts for the products already purchased by a given commerce account.

**For example:** If an account has bought the *Machine A*, and there are other products in the catalog like *Retrofit Part 1 for Machine A*, *Retrofit Part 2 for Machine A*, etc... this datasource will show those retrofit parts.

This datasource is best used in combination with the [My Products Datasource](https://sales.liferay.com/en/web/library/-/my-purchased-products-datasource-for-product-publisher?redirect=%2Fgroup%2Flibrary%2Fdemo-assets%2F-%2Fcategories%2F323650%3Fp_r_p_resetCur%3Dtrue%26p_r_p_categoryId%3D323650) to set up a page like this, where a customer can see their current purchased machines and a list of the available retrofitting parts for their machines:

![Freelancer](retrofit-datasource.png
)



In order for this to work, you need to create those product relations using a product relation type called *"retrofit"*. For example, editing the product Machine A , and in the Product Relations tab add some relations of type "retrofit" with other products of the catalog.

The module includes what is needed to create automatically the new Product Relation Type in the System Settings when deploying the OSGi module, but due to this  [bug](https://issues.liferay.com/browse/LPS-101642) , this is not working.

Therefore you need to create this Product Relation Type manually in `System Settings > Catalog > Product Relations`  

**When this bug is solved, you won't need to create the product relation type manually.**


Developed to run on the following versions of Liferay and/or Commerce: `Liferay DXP 7.3-GA1` or `Liferay DXP 7.3-SP1` with `Commerce 3.0`


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

