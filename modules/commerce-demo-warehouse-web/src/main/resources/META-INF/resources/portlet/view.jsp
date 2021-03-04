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
<%@ include file="../init.jsp" %>
<%
 	/* request = PortalUtil.getOriginalServletRequest(request);  */
	 double latitude = ParamUtil.get(request, "latitude", 40.333212837747965);
	double longitude = ParamUtil.get(request, "longitude", -3.5489682371781845);
	CommerceContext commerceContext = (CommerceContext)request.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);
	CommerceOrder commerceOrder = null;
	if (commerceContext != null) {
		 commerceOrder = commerceContext.getCommerceOrder();
		}
	if (commerceOrder == null) {
		commerceOrder = CommerceOrderLocalServiceUtil.addCommerceOrder(themeDisplay.getUserId() , commerceContext.getCommerceChannelGroupId(),  commerceContext.getCommerceAccount().getCommerceAccountId());
	}
	
	List<CommerceInventoryWarehouse> commerceWarehouses = 
		CommerceInventoryWarehouseLocalServiceUtil.getCommerceInventoryWarehouses(themeDisplay.getCompanyId(), true, 
			 -1, -1, null);
	CPContentHelper cpContentHelper = (CPContentHelper)request.getAttribute(CPContentWebKeys.CP_CONTENT_HELPER);
	CPCatalogEntry cpCatalogEntry = cpContentHelper.getCPCatalogEntry(request);
	
	List<CPSku> cpSkus = cpCatalogEntry.getCPSkus();
	CPInstance cpInstance = 
			CPInstanceLocalServiceUtil.fetchCPInstance(cpSkus.get(0).getCPInstanceId());
	List<CPWarehouse> cPwarehouses = (List<CPWarehouse>) request.getAttribute("cPwarehouses");

%>
<c:if test="<%= cPwarehouses!=null && !cPwarehouses.isEmpty()%>">
<div class="commerce-demo-warehouse-web">
<div class="row">
	<div class="col-xl-8 commerce-table-container" id="<portlet:namespace />entriesContainer">
		<portlet:actionURL name="addCommerceWarehouseOrderItem" var="addCommerceWarehouseOrderItemURL" />
		<aui:form action="<%= addCommerceWarehouseOrderItemURL %>" cssClass="hide" name="fm">
			<aui:input name="warehouseId" type="hidden" value="" />
			<aui:input name="cpInstanceId" type="hidden" value="<%= cpCatalogEntry.getCPSkus().get(0).getCPInstanceId() %>" />
			<aui:input name="quantity" type="hidden" value="1" />
		</aui:form>

	<div class="table-responsive">
			<table class="table table-striped table-hover">
					<colgroup>
						<col style="width: 40%">
						<col style="width: 20%">
						<col style="width: 10%">
						<col style="width: 25%">
					</colgroup>
					<tbody>
						<tr>
							<th><%=LanguageUtil.get(resourceBundle, "plant")%></th>
							<th><%=LanguageUtil.get(resourceBundle, "price")%></th>
							<th><%=LanguageUtil.get(resourceBundle, "directions")%></th>
							<th></th>
						</tr>

						<%
							for (CPWarehouse cPWarehouse : cPwarehouses) {
						%>

						<tr>
							<td>
								<h1 class="table-list-title">
									<span class="text-truncate">
										<%=HtmlUtil.escape(cPWarehouse.getName())%>
									<br>
									<p style="font-weight: normal">
									<%=HtmlUtil.escape(cPWarehouse.getAddress())%>
									<br>
									<%=HtmlUtil.escape(cPWarehouse.getPhoneNumber())%>
									</p>
									</span>
								</h1>
							</td>
							<td>
							<span class="text-truncate">
								<span class="price price--big"><%=HtmlUtil.escape(cPWarehouse.getPrice())%></span>
							</span>
							</td>
							<td>
							<span class="text-truncate">
								<a href="<%=cPWarehouse.getDirectionsUrl()%>" target="_blank">
								<img src="https://cdn.iconscout.com/icon/free/png-256/directions-1782209-1512759.png" style="width:50px">
								</a>
							</span>
							</td>
							<td>
							<span class="text-truncate">
							 <button c-warehouse-id="<%=cPWarehouse.getWarehouseId()%>" class="btn btn-lg btn-primary taglib-add-to-cart" cp-instance-id="<%=cPWarehouse.getInstanceId()%>" id="<%=cpInstance.getCPInstanceId()%>" name="addToCart" type="submit">
							 <%=LanguageUtil.get(request, "add-to-cart")%>
							</button>
							</span>
							</td>
						</tr>

						<%
							}
						%>
					</tbody>
				</table>
			</div>
	

	</div>
	<div class="col-xl-4">
		<div id="map" style="height:100%; min-height: 300px"></div>
		<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCntRHjBtGC_KAJqwA5pXmkVq0nUheIJgI"></script>
		<script>
			var map = new google.maps.Map(document.getElementById('map'), {
				zoom: 12,
				center: {lat: <%= latitude %>, lng: <%= longitude %>},
				disableDefaultUI: false
			});
			var infoWindow = new google.maps.InfoWindow;
			var canChangeMapView = false;
			<%
			if (latitude == Double.MAX_VALUE || longitude == Double.MAX_VALUE) {
			%>
				if (navigator.geolocation) {
					navigator.geolocation.getCurrentPosition(function(position) {
						var pos = {
							lat: position.coords.latitude,
							lng: position.coords.longitude
						};
						marker = new google.maps.Marker({
							name: "myLocation",
							position: pos,
							map: map,
							icon: {
								url: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
							}
						});
						google.maps.event.addListener(marker, 'click', (function(marker, i) {
							return function() {
								infoWindow.setContent("My Location");
								infoWindow.open(map, marker);
							}
						})(marker, i));
						map.setCenter(pos);
					}, function() {
						handleLocationError(true, infoWindow, map.getCenter());
					});
				} else {
					// Browser doesn't support Geolocation
					handleLocationError(false, infoWindow, map.getCenter());
				}
			<%
			}
			%>
			var marker;
			var i = 0;
			var markerscoordinates = [];
			<%
				for (CommerceInventoryWarehouse commerceWarehouse : commerceWarehouses) {
					ExpandoBridge expandoBridge = commerceWarehouse.getExpandoBridge();
					Map<String, Serializable> attributes = expandoBridge.getAttributes();
					long commercePriceListId = (Long)attributes.get("priceListId");
					CommercePriceEntry commercePriceEntry =
						CommercePriceEntryLocalServiceUtil.fetchCommercePriceEntry(commercePriceListId, cpInstance.getCPInstanceUuid());
					if (commercePriceEntry != null) {
						%>
							markerscoordinates.push(new google.maps.LatLng(<%= commerceWarehouse.getLatitude() %>, <%= commerceWarehouse.getLongitude() %>));
							marker = new google.maps.Marker({
								position: new google.maps.LatLng({lat: <%= commerceWarehouse.getLatitude() %>, lng: <%= commerceWarehouse.getLongitude() %>}),
								map: map,
								label: (i + 1).toString()
							});
							i += 1;
							canChangeMapView = true;
						<%
					}
				}
			%>
			if (canChangeMapView) {
				// Set Zoom
				var latlngbounds = new google.maps.LatLngBounds();
				markerscoordinates.forEach(function(markercoordinates) {
					latlngbounds.extend(markercoordinates);
				});
				map.fitBounds(latlngbounds);
				var listener = google.maps.event.addListener(map, "idle", function() {
					if (map.getZoom() > 12) {
						map.setZoom(12);
					}
					google.maps.event.removeListener(listener);
				});
			}
		</script>
	</div>
</div>
</div>

<aui:script use="aui-base,liferay-notification">
	var instance = this;
	$('button[name="addToCart"]').on(
		'click',
		function(event) {
			event.preventDefault();
			$('#<portlet:namespace />warehouseId').val(event.target.getAttribute("c-warehouse-id"));
			const formData = new FormData();
			formData.append('commerceAccountId', <%= commerceContext.getCommerceAccount().getCommerceAccountId() %>);
			formData.append('groupId', <%= themeDisplay.getScopeGroupId() %>);
			formData.append('productId', <%= cpCatalogEntry.getCPSkus().get(0).getCPInstanceId() %>);
			formData.append('quantity', 1);
			formData.append('options', '[]');
			formData.append('orderId', <%= commerceOrder.getCommerceOrderId() %>);
			fetch(
				'<%= PortalUtil.getPortalURL(request) + "/o/commerce-ui/cart-item" %>',
				{
					body: formData,
					method: 'POST'
				}
			).then(
				response => response.json()
			).then(
				(jsonresponse) => {
					var message, type;
					if (jsonresponse.success) {
						Liferay.fire('updateCart', jsonresponse);
						message = 'The Product Was Successfully Added to The Cart';
						type = 'success';
					}
					else {
						type = 'danger';
						var validatorErrors = jsonresponse.validatorErrors;
						if (validatorErrors) {
							validatorErrors.forEach(
								function(validatorError) {
									message = validatorError.message;
								}
							);
						}
						else {
							message = jsonresponse.errorMessages[0];
						}
					}
					new Liferay.Notification(
						{
							closeable: true,
							delay: {
								hide: 5000,
								show: 0
							},
							duration: 500,
							message: message,
							render: true,
							title: '',
							type: type
						}
					);
				}
			);
		}
	);
</aui:script>
</c:if>