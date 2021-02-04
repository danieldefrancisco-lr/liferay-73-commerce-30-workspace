<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="./init.jsp" %>

<%
CPContentHelper cpContentHelper = (CPContentHelper)request.getAttribute(CPContentWebKeys.CP_CONTENT_HELPER);
CPCatalogEntry cpCatalogEntry = cpContentHelper.getCPCatalogEntry(request);
CPSku cpSku = cpContentHelper.getDefaultCPSku(cpCatalogEntry);
long cpDefinitionId = cpCatalogEntry.getCPDefinitionId();
String longitude = (String) request.getAttribute("longitude");
String latitude = (String) request.getAttribute("latitude");
String productContentAuthToken = AuthTokenUtil.getToken(request, plid, CPPortletKeys.CP_CONTENT_WEB);
boolean addedToWishlist = (boolean) request.getAttribute("addedToWishlist");
String wishlistAPI = (String) request.getAttribute("wishlistAPI");
Long commerceAccountId = (Long) request.getAttribute("commerceAccountId");

%>

<link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css"
   integrity="sha512-xodZBNTC5n17Xt2atTPuE1HxjVMSvLVW9ocqUKLsCC5CXdbqCmblAshOMAS6/keqq/sMZMZ19scR4PsZChSR7A=="
   crossorigin=""/>
<!-- Make sure you put this AFTER Leaflet's CSS -->
 <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"
   integrity="sha512-XQoYMqMTK8LvdxXYG3nZ448hOEQiglfqkJs1NOQV44cWnUrBc8PkAOcXy20w0vlaXaVUearIOBhiXZ5V3ynxwA=="
   crossorigin=""></script> 

<link href="/o/commerce-demo-realestate-renderer/css/main.css" rel="stylesheet" type="text/css"  /> 

<div class="container-fluid product-detail" id="<portlet:namespace /><%= cpCatalogEntry.getCPDefinitionId() %>ProductContent" style="padding-left:25px">
	<div class="product-detail-header">
		<div class="row">
			<div class="col-lg-9 col-md-7">
				<div class="row product-name">
					<h1><%= HtmlUtil.escape(cpCatalogEntry.getName()) %></h1>
				</div>
				<div class="row product-short-description">
					<p><%= cpCatalogEntry.getShortDescription()%></p>
				</div>
				<div class="row">	
					<div class="col-lg-10 col-md-9 col-xs-10 full-image">
						<c:if test="<%= Validator.isNotNull(cpCatalogEntry.getDefaultImageFileUrl()) %>">
							<img class="center-block img-responsive" id="<portlet:namespace />full-image" src="<%= cpCatalogEntry.getDefaultImageFileUrl() %>" />
						</c:if>
					</div>
				</div>
			</div>

			<div class="col-lg-3 col-md-5">
				<c:choose>
					<c:when test="<%= cpSku != null %>">
						<div class="price"><h1><liferay-commerce:price CPDefinitionId="<%= cpDefinitionId %>" CPInstanceId="<%= cpSku.getCPInstanceId() %>" /></h1></div>
					</c:when>
					<c:otherwise>
						<div class="price" data-text-cp-instance-price=""></div>
					</c:otherwise>
				</c:choose>

				<div class="row">
					<div class="col-md-12">
						<div class="options">
							<%= cpContentHelper.renderOptions(renderRequest, renderResponse) %>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-12">
						<liferay-commerce:compare-product CPDefinitionId="<%= cpDefinitionId %>" />
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-12">
				    			<span class="guardar-favorito">
					<a href="javascript:void(0);" id="60450003" title="Añadir a favoritos" class="sh_icon-heart save-property" onclick="_handleWishListButtonClick()">
					<span class="no-save-heart">Guardar <span class="fav">favorito</span>
					<liferay-ui:icon
    				iconCssClass="icon-heart"/>
    				</span>
<!-- 					<span class="save-heart">Guardado </span>-->
					</a>
					</span>	
					</div>
				</div>
				
			</div>
		</div>
	</div>

 <div class="row">	
<div class="basicdata" style= "background-color: #fff; padding: 25px 0;">
    <div class="container">
       <div class="basicdata-info" style="float:left">
		<div class="basicdata-item" style="display: inline-block; margin-right: 25px; line-height: 25px;">
				<liferay-ui:icon
    				iconCssClass="icon-info-sign"
    				label="<%= true %>"
    				message="96 m2"/>
            </div>
			<div class="basicdata-item" style="display: inline-block; margin-right: 25px; line-height: 25px;">
				<liferay-ui:icon
    				iconCssClass="icon-home"
    				label="<%= true %>"
    				message="2 rooms"/>
            </div>
			<div class="basicdata-item" style="display: inline-block; margin-right: 25px; line-height: 25px;">
				<liferay-ui:icon
    				iconCssClass="icon-plus"
    				label="<%= true %>"
    				message="1"/>
    		 </div>
			<div class="basicdata-item" style="display: inline-block; margin-right: 25px; line-height: 25px;">
				<liferay-ui:icon
    				iconCssClass="icon-eur"
    				label="<%= true %>"
    				message="1.390 eur/m2"/>
            </div>
        <div class="clear"></div>
    </div>

</div>
</div>
</div> 


	<%
	List<CPDefinitionSpecificationOptionValue> cpDefinitionSpecificationOptionValues = cpContentHelper.getCPDefinitionSpecificationOptionValues(cpDefinitionId);
	List<CPOptionCategory> cpOptionCategories = cpContentHelper.getCPOptionCategories(company.getCompanyId());
	List<CPMedia> cpMediaEntries = cpContentHelper.getCPAttachmentFileEntries(cpDefinitionId, themeDisplay);
	%>

	<div class="row">
		<div class="product-detail-body w-100">
		
			<div class="nav-tabs-centered">
				<liferay-ui:tabs
			names="Description, Specifications, Downloads"
			param="tabs2" refresh="<%=false%>" type="nav nav-underline">

			<liferay-ui:section>
				<p><%= cpCatalogEntry.getDescription() %></p>
			</liferay-ui:section>

			<liferay-ui:section>
				<c:if test="<%= cpContentHelper.hasCPDefinitionSpecificationOptionValues(cpDefinitionId) %>">
						<div class="tab-pane" id="<portlet:namespace />specification">
							<c:if test="<%= !cpDefinitionSpecificationOptionValues.isEmpty() %>">
								<div class="table-responsive">
									<table class="table table-bordered table-striped">

										<%
										for (CPDefinitionSpecificationOptionValue cpDefinitionSpecificationOptionValue : cpDefinitionSpecificationOptionValues) {
											CPSpecificationOption cpSpecificationOption = cpDefinitionSpecificationOptionValue.getCPSpecificationOption();
										%>

											<tr>
												<td><%= HtmlUtil.escape(cpSpecificationOption.getTitle(languageId)) %></td>
												<td><%= HtmlUtil.escape(cpDefinitionSpecificationOptionValue.getValue(languageId)) %></td>
											</tr>

										<%
										}
										%>

									</table>
								</div>
							</c:if>

							<%
							for (CPOptionCategory cpOptionCategory : cpOptionCategories) {
								List<CPDefinitionSpecificationOptionValue> categorizedCPDefinitionSpecificationOptionValues = cpContentHelper.getCategorizedCPDefinitionSpecificationOptionValues(cpDefinitionId, cpOptionCategory.getCPOptionCategoryId());
							%>

								<c:if test="<%= !categorizedCPDefinitionSpecificationOptionValues.isEmpty() %>">
									<div class="table-responsive">
										<table class="table table-bordered ">
										<colgroup>
    										<col style="width:50%">
    										<col style="width:50%">
    									</colgroup>
    									<tbody>
											<tr>
												<th><%= cpOptionCategory.getTitle(languageId) %></th>
												<th></th>
											</tr>

											<%
											for (CPDefinitionSpecificationOptionValue cpDefinitionSpecificationOptionValue : categorizedCPDefinitionSpecificationOptionValues) {
												CPSpecificationOption cpSpecificationOption = cpDefinitionSpecificationOptionValue.getCPSpecificationOption();
											%>

												<tr>
													<td><%= HtmlUtil.escape(cpSpecificationOption.getTitle(languageId)) %></td>
													<td><%= HtmlUtil.escape(cpDefinitionSpecificationOptionValue.getValue(languageId)) %></td>
												</tr>

											<%
											}
											%>
										</tbody>
										</table>
									</div>
								</c:if>

							<%
							}
							%>

						</div>
					</c:if>
			</liferay-ui:section>

			<liferay-ui:section>
				<c:if test="<%= !cpMediaEntries.isEmpty() %>">
						<div class="tab-pane" id="<portlet:namespace />attachments">
							<div class="table-responsive">
								<table class="table table-bordered table-striped">

									<%
									for (CPMedia curCPMedia : cpMediaEntries) {
									%>

										<tr>
											<td>
												<span><%= curCPMedia.getTitle() %></span>

												<span>
													<aui:icon cssClass="icon-monospaced" image="download" markupView="lexicon" target="_blank" url="<%= curCPMedia.getDownloadUrl() %>" />
												</span>
											</td>
										</tr>

									<%
									}
									%>

								</table>
							</div>
						</div>
					</c:if>
			</liferay-ui:section>
		</liferay-ui:tabs>
	</div>

	</div>
	</div>
		
		<div class="row">
		<div id="product-location "class="props_features">
		<h2><%= LanguageUtil.get(resourceBundle, "location") %></h2>
		 <div id="mapid" style="width: 600px; height: 400px; position: relative; outline: none;"></div>
		</div>
		
	</div>
</div>
<aui:script>
	window.document.addEventListener('DOMContentLoaded', function () {
		var thumbElements = window.document.querySelectorAll('.thumb');

		Array.from(thumbElements).forEach(function (thumbElement) {
			thumbElement.addEventListener('click', function (event) {
				window.document
					.querySelector('#<portlet:namespace />full-image')
					.setAttribute(
						'src',
						event.currentTarget.getAttribute('data-url')
					);
			});
		});
	});
</aui:script>

<liferay-portlet:actionURL name="checkCPInstance" portletName="com_liferay_commerce_product_content_web_internal_portlet_CPContentPortlet" var="checkCPInstanceURL">
	<portlet:param name="cpDefinitionId" value="<%= String.valueOf(cpDefinitionId) %>" />
	<portlet:param name="groupId" value="<%= String.valueOf(themeDisplay.getScopeGroupId()) %>" />
</liferay-portlet:actionURL>

<aui:script use="liferay-commerce-product-content">
	var productContent = new Liferay.Portlet.ProductContent({
		checkCPInstanceActionURL: '<%= checkCPInstanceURL %>',
		cpDefinitionId: <%= cpDefinitionId %>,
		fullImageSelector: '#<portlet:namespace />full-image',
		namespace: '<portlet:namespace />',
		productContentAuthToken: '<%= productContentAuthToken %>',
		productContentSelector:
			'#<portlet:namespace /><%= cpDefinitionId %>ProductContent',
		thumbsContainerSelector: '#<portlet:namespace />thumbs-container',
		viewAttachmentURL:
			'<%= String.valueOf(cpContentHelper.getViewAttachmentURL(liferayPortletRequest, liferayPortletResponse)) %>',
	});

	Liferay.component(
		'<portlet:namespace /><%= cpDefinitionId %>ProductContent',
		productContent
	);
</aui:script>

<aui:script>
	var mymap = L.map('mapid').setView([<%= latitude %>, <%= longitude %>], 13);
	L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox/streets-v11',
    tileSize: 512,
    zoomOffset: -1,
    accessToken: 'pk.eyJ1IjoiZGFuaWVsZGVmcmFuY2lzY28iLCJhIjoiY2traTRwcmU2MDM3MzJ3czd0YXZraGRyOSJ9.-P-IRN57WdhI0dQAReqSkg'
}).addTo(mymap);

var circle = L.circle([<%= latitude %>, <%= longitude %>], {
    color: 'blue',
    fillColor: '#99ccff',
    fillOpacity: 0.5,
    radius: 800
}).addTo(mymap);
</aui:script>
<script>
		function toggleInWishList(params) {
			var accountId = params.accountId;
			var cpDefinitionId = params.cpDefinitionId;
			var skuId = params.skuId;
			if (!cpDefinitionId) {
				return Promise.resolve({success: false});
			}
			const formData = new FormData();
			formData.append('commerceAccountId', accountId);
			formData.append('groupId', Liferay.ThemeDisplay.getScopeGroupId());
			formData.append('productId', cpDefinitionId);
			formData.append('skuId', skuId);
			formData.append('options', '[]');
			return fetch('/o/commerce-ui/wish-list-item', {
				body: formData,
				credentials: 'include',
				headers: new Headers({'x-csrf-token': window.Liferay.authToken}),
				method: 'POST',
			})
				.then((response) => response.json())
				.catch(() => ({success: false}))
				.then((response) => {
					if (response.success) {
						var buttonElement = document.querySelector('minium-card__add-to-wishlist-button');
						buttonElement.classList.toggle('minium-card__add-to-wishlist-button--added')
					}
				});
		}
</script>