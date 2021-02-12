package commerce.demo.warehouse.web.portlet;

import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseLocalService;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseService;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalService;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.catalog.CPSku;
import com.liferay.commerce.product.constants.CPWebKeys;
import com.liferay.commerce.product.content.constants.CPContentWebKeys;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.product.util.CPDefinitionHelper;
import com.liferay.commerce.service.CommerceCountryLocalService;
import com.liferay.commerce.service.CommerceCountryService;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import commerce.demo.warehouse.web.constants.CommerceDemoWarehouseWebPortletKeys;
import commerce.demo.warehouse.web.model.CPWarehouse;

/**
 * @author dfrancisco
 */
@Component(immediate = true, property = { 
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css", 
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Commerce Demo Warehouse Web", 
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/portlet/view.jsp",
		"javax.portlet.name=" + CommerceDemoWarehouseWebPortletKeys.COMMERCEDEMOWAREHOUSEWEB,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user" }, 
		service = Portlet.class)
public class CommerceDemoWarehouseWebPortlet extends MVCPortlet {

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {


		renderRequest.setAttribute(CPContentWebKeys.CP_CONTENT_HELPER, _cpContentHelper);
		
		CPCatalogEntry cpCatalogEntry =
				(CPCatalogEntry)renderRequest.getAttribute(
					CPWebKeys.CP_CATALOG_ENTRY);

		List<CPWarehouse> cPwarehouses = new ArrayList<>();
		try {
			cPwarehouses = getItems(cpCatalogEntry, renderRequest);
		} catch (PortalException e) {
			throw new PortletException(e);
		}

		renderRequest.setAttribute("cPwarehouses", cPwarehouses);

		super.render(renderRequest, renderResponse);
	}

	private static final Log _log = LogFactoryUtil.getLog(CommerceDemoWarehouseWebPortlet.class);

	private List<CPWarehouse> getItems(CPCatalogEntry cpCatalogEntry, RenderRequest request)
			throws PortalException {

		List<CPWarehouse> CPWarehouses = new ArrayList<>();

		if (cpCatalogEntry != null) {
			ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

			CommerceContext commerceContext = (CommerceContext) request
					.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);

			double latitude = ParamUtil.get(request, "latitude", 32.9345787);
			double longitude = ParamUtil.get(request, "longitude", -117.1124716);

		if (latitude == Double.MAX_VALUE || longitude == Double.MAX_VALUE) {
			try {
				JSONObject jo = JSONFactoryUtil.createJSONObject(
						HttpUtil.URLtoString("http://ip-api.com/json/" + _portal.getHttpServletRequest(request).getRemoteAddr()));
				latitude = jo.getDouble("lat");
				longitude = jo.getDouble("lon");
			} catch (Exception e) {

			}
		}
			List<CommerceInventoryWarehouse> commerceWarehouses = _commerceWarehouseLocalService
					.getCommerceInventoryWarehouses(themeDisplay.getCompanyId());

			for (CommerceInventoryWarehouse commerceWarehouse : commerceWarehouses) {
				
				ExpandoBridge expandoBridge = commerceWarehouse.getExpandoBridge();
				Map<String, Serializable> attributes = expandoBridge.getAttributes();
				
				long commercePriceListId = (Long) attributes.get("priceListId");
				String price = getPrice(cpCatalogEntry, commerceContext, commercePriceListId, "price",
						themeDisplay.getLocale());
				String promoPrice = getPrice(cpCatalogEntry, commerceContext, commercePriceListId, "promo",
						themeDisplay.getLocale());

				if (!Validator.isBlank(price)) {
					CPWarehouses.add(new CPWarehouse(commerceWarehouse, cpCatalogEntry, price, promoPrice,
							new Double[] { latitude, longitude }, attributes));
				}
			}
		}
		return ListUtil.sort(CPWarehouses, Comparator.naturalOrder());
	}

	protected String getPrice(CPCatalogEntry cpCatalogEntry, CommerceContext commerceContext, long commercePriceListId,
			String type, Locale locale) throws PortalException {

		CommerceCurrency commerceCurrency = commerceContext.getCommerceCurrency();

		List<CPSku> cpSkus = cpCatalogEntry.getCPSkus();

		CPInstance cpInstance = _cpInstanceLocalService.fetchCPInstance(cpSkus.get(0).getCPInstanceId());

		CommercePriceEntry commercePriceEntry = _commercePriceEntryLocalService
				.fetchCommercePriceEntry(commercePriceListId, cpInstance.getCPInstanceUuid());

		if (commercePriceEntry == null) {
			return StringPool.BLANK;
		}

		CommerceMoney commerceMoney = commercePriceEntry.getPriceMoney(commerceCurrency.getCommerceCurrencyId());

		if (type.equals("promo")) {
			commerceMoney = commercePriceEntry.getPromoPriceMoney(commerceCurrency.getCommerceCurrencyId());
		}
		String returnValue = null;
		if (commerceMoney.getPrice() != null) {
			returnValue = _commercePriceFormatter.format(commerceCurrency, commerceMoney.getPrice(), locale);
		}
		return returnValue;
	}

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceCountryLocalService _commerceCountryLocalService;

	@Reference
	private CommercePriceEntryLocalService _commercePriceEntryLocalService;

	@Reference
	private CommercePriceFormatter _commercePriceFormatter;

	@Reference
	private CommerceInventoryWarehouseLocalService _commerceWarehouseLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private CPDefinitionHelper _cPDefinitionHelper;

	@Reference
	CommerceCountryService _commerceCountryService;

	@Reference
	CommerceInventoryWarehouseService _commerceWarehouseService;

	@Reference
	private CPContentHelper _cpContentHelper;

	@Reference
	private Portal _portal;
}