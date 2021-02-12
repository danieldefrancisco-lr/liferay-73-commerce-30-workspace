package commerce.demo.warehouse.web.frontend;

import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseLocalService;
import com.liferay.commerce.model.CommerceCountry;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalService;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.catalog.CPSku;
import com.liferay.commerce.product.content.constants.CPContentWebKeys;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.commerce.product.display.context.util.CPRequestHelper;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.product.util.CPDefinitionHelper;
import com.liferay.commerce.service.CommerceCountryLocalService;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.frontend.taglib.clay.data.Filter;
import com.liferay.frontend.taglib.clay.data.Pagination;
import com.liferay.frontend.taglib.clay.data.set.ClayDataSetActionProvider;
import com.liferay.frontend.taglib.clay.data.set.ClayDataSetDisplayView;
import com.liferay.frontend.taglib.clay.data.set.provider.ClayDataSetDataProvider;
import com.liferay.frontend.taglib.clay.data.set.view.table.BaseTableClayDataSetDisplayView;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchema;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaBuilder;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaBuilderFactory;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaField;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import commerce.demo.warehouse.web.model.CPWarehouse;

/**
 * @author Alec Sloan
 */
@Component(
    immediate = true,
    property = {
    				"clay.data.provider.key=" + CPWarehouseClayTable.NAME,
    				"clay.data.set.display.name=" + CPWarehouseClayTable.NAME
    			},
    service = {
    		ClayDataSetActionProvider.class, ClayDataSetDataProvider.class,
    		ClayDataSetDisplayView.class
    }
)
 public class CPWarehouseClayTable
 extends BaseTableClayDataSetDisplayView
    implements ClayDataSetDataProvider<CPWarehouse>,
    ClayDataSetActionProvider {

    public static final String NAME = "cpWarehouses";

    @Override
    public List<DropdownItem> getDropdownItems(
        HttpServletRequest httpServletRequest, long groupId, Object model)
        throws PortalException {

        return new ArrayList<>();
    }

    @Override
    public int getItemsCount(HttpServletRequest httpServletRequest, Filter filter)
        throws PortalException {

        ThemeDisplay themeDisplay =
            (ThemeDisplay)httpServletRequest.getAttribute(
                WebKeys.THEME_DISPLAY);
        
        CPRequestHelper _cpRequestHelper = new CPRequestHelper(httpServletRequest);
        CommerceCountry commerceCountry =
        		_commerceCountryLocalService.getCommerceCountry(
        				themeDisplay.getCompanyId(), themeDisplay.getLocale().getCountry());

//        return _commerceWarehouseLocalService.getCommerceInventoryWarehousesCount(
//        		themeDisplay.getCompanyId(), true,
//            commerceCountry.getTwoLettersISOCode());
        
        return _commerceWarehouseLocalService.getCommerceInventoryWarehousesCount(
        		themeDisplay.getCompanyId(), true);
    }

    @Override
    public ClayTableSchema getClayTableSchema() {
        ClayTableSchemaBuilder clayTableSchemaBuilder =
        		_clayTableSchemaBuilderFactory.create();

        ClayTableSchemaField vendorField = clayTableSchemaBuilder.addClayTableSchemaField(
            "name", "vendor");

          vendorField.setContentRenderer("contactDetail");

//        clayTableSchemaBuilder.addClayTableSchemaField("hoursOfOperation", "hours of operation");

        ClayTableSchemaField priceField = clayTableSchemaBuilder.addClayTableSchemaField(
            "price", "price");

        priceField.setContentRenderer("price");

        ClayTableSchemaField directionsField = clayTableSchemaBuilder.addClayTableSchemaField(
            "directionsUrl", "directions");

        //directionsField.setContentRenderer("directionsButton");

        clayTableSchemaBuilder.addClayTableSchemaField("price", "sortable");

        ClayTableSchemaField addToCartField = clayTableSchemaBuilder.addClayTableSchemaField(
            "addToCart", "");

        addToCartField.setContentRenderer("addToCartButton");

        return clayTableSchemaBuilder.build();
    }


    @Override
    public List<CPWarehouse> getItems(
        HttpServletRequest httpServletRequest, Filter filter,
        Pagination pagination, Sort sort)
        throws PortalException {
         
        ThemeDisplay themeDisplay =
            (ThemeDisplay)httpServletRequest.getAttribute(
                WebKeys.THEME_DISPLAY);
        
        long cpDefinitionId = ParamUtil.getLong(
    			httpServletRequest, "cpDefinitionId");
        
        long commerceAccountId = ParamUtil.getLong(
    			httpServletRequest, "commerceAccountId");
        
        System.out.println("CPDefinitionId: " + cpDefinitionId);
        CPContentHelper cpContentHelper =
                (CPContentHelper)httpServletRequest.getAttribute(
                    CPContentWebKeys.CP_CONTENT_HELPER);
        
        CommerceCountry commerceCountry =
                _commerceCountryLocalService.getCommerceCountry(
                    themeDisplay.getCompanyId(), "US");

        List<CPWarehouse> CPWarehouses = new ArrayList<>();

        CommerceContext commerceContext =
				(CommerceContext)httpServletRequest.getAttribute(
					CommerceWebKeys.COMMERCE_CONTEXT);      
        
        CPCatalogEntry cpCatalogEntry =
        		_cPDefinitionHelper.getCPCatalogEntry(commerceAccountId, 
        				themeDisplay.getScopeGroupId(), cpDefinitionId, themeDisplay.getLocale());
        
        httpServletRequest = PortalUtil.getOriginalServletRequest(httpServletRequest);

        double latitude = ParamUtil.get(httpServletRequest, "latitude", 32.9345787);
        double longitude = ParamUtil.get(httpServletRequest, "longitude", -117.1124716);

        if (latitude == Double.MAX_VALUE || longitude == Double.MAX_VALUE) {
            try {
                JSONObject jo = JSONFactoryUtil.createJSONObject(HttpUtil.URLtoString("http://ip-api.com/json/" + httpServletRequest.getRemoteAddr()));
                latitude = jo.getDouble("lat");
                longitude = jo.getDouble("lon");
            }
            catch (Exception e){

            }
        }
//
//        List<CommerceInventoryWarehouse> commerceWarehouses =
//            _commerceWarehouseLocalService.searchCommerceInventoryWarehouses(themeDisplay.getCompanyId(), true, commerceCountry.getTwoLettersISOCode(), "", 
//            		pagination.getStartPosition(), pagination.getEndPosition(), sort);
        
        List<CommerceInventoryWarehouse> commerceWarehouses =
                _commerceWarehouseLocalService.getCommerceInventoryWarehouses(themeDisplay.getCompanyId(), true, 
                		pagination.getStartPosition(), pagination.getEndPosition(), null);


        for (CommerceInventoryWarehouse commerceWarehouse : commerceWarehouses) {

            ExpandoBridge expandoBridge = commerceWarehouse.getExpandoBridge();

            Map<String, Serializable> attributes = expandoBridge.getAttributes();

            long commercePriceListId = (Long)attributes.get("priceListId");

            String price = getPrice(cpCatalogEntry, commerceContext,
                commercePriceListId, "price", themeDisplay.getLocale());

            String promoPrice = getPrice(cpCatalogEntry, commerceContext,
                commercePriceListId, "promo", themeDisplay.getLocale());

            if (!Validator.isBlank(price)) {
                CPWarehouses.add(
                    new CPWarehouse(
                        commerceWarehouse, cpCatalogEntry, price, promoPrice,
                        new Double[]{latitude, longitude}, attributes));
            }
        }

        return ListUtil.sort(CPWarehouses, Comparator.naturalOrder());
    }

    protected String getPrice(
            CPCatalogEntry cpCatalogEntry, CommerceContext commerceContext,
            long commercePriceListId, String type, Locale locale)
        throws PortalException {

        CommerceCurrency commerceCurrency =
            commerceContext.getCommerceCurrency();

        List<CPSku> cpSkus = cpCatalogEntry.getCPSkus();

        CPInstance cpInstance =
            _cpInstanceLocalService.fetchCPInstance(cpSkus.get(0).getCPInstanceId());

        CommercePriceEntry commercePriceEntry =
            _commercePriceEntryLocalService.fetchCommercePriceEntry(
                commercePriceListId, cpInstance.getCPInstanceUuid());

        if (commercePriceEntry == null) {
            return StringPool.BLANK;
        }

        CommerceMoney commerceMoney = commercePriceEntry.getPriceMoney(
            commerceCurrency.getCommerceCurrencyId());

        if (type.equals("promo")) {
            commerceMoney = commercePriceEntry.getPromoPriceMoney(
                commerceCurrency.getCommerceCurrencyId());
        }
        System.out.println("Price:"+ commerceMoney.getPrice());
        String returnValue = null;
        if (commerceMoney.getPrice() != null) {
        	returnValue = _commercePriceFormatter.format(
            commerceCurrency, commerceMoney.getPrice(), locale);
        } 
        return returnValue;
    }

    @Reference
    private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

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
    private Portal _portal;
    
    @Reference
	private CPDefinitionHelper _cPDefinitionHelper;

}
