package related.document.info.list.provider;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.commerce.product.service.CPAttachmentFileEntryLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.info.list.provider.InfoListProvider;
import com.liferay.info.list.provider.InfoListProviderContext;
import com.liferay.info.pagination.Pagination;
import com.liferay.info.sort.Sort;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import commerce.custom.service.api.CommerceCustomService;

/**
 * @author Dani
 */
@Component(service = InfoListProvider.class)
public class RelatedDocumentInfoListProvider implements InfoListProvider<AssetEntry> {

	@Override
	public List<AssetEntry> getInfoList(InfoListProviderContext infoListProviderContext) {
		
		List<AssetEntry> results = new ArrayList<AssetEntry>();
		try {

			List<CPCatalogEntry> cpCatalogEntries = getPurchasedProductsByAccount();
			List<CPAttachmentFileEntry> attachments = new ArrayList<CPAttachmentFileEntry>();
			
		//Get all the attachment files of the products purchased by the selected commerce account
			for (CPCatalogEntry cpCatalogEntry : cpCatalogEntries) {
				attachments.addAll(_cpAttachmentFileEntryLocalServivce.getCPAttachmentFileEntries(new Long(41805).longValue(),
						cpCatalogEntry.getCPDefinitionId(), 1, 0, 0, Integer.MAX_VALUE));
			}
       
		//Get the AssetEntry associated with each file
			for (CPAttachmentFileEntry cpAttachmentFileEntry : attachments) {
				//IF to avoid duplicates
				if (!results.contains(_assetEntryLocalService.getEntry(dlFileEntryClassName, cpAttachmentFileEntry.getFileEntryId())))
				results.add(
						_assetEntryLocalService.getEntry(dlFileEntryClassName, cpAttachmentFileEntry.getFileEntryId()));
			}
		} catch (PortalException e) {
			e.printStackTrace();
		}

		// return _assetEntryLocalService.getAssetEntries(0, 5);
		return results;
	}

	@Override
	public List<AssetEntry> getInfoList(InfoListProviderContext infoListProviderContext, Pagination pagination,
			Sort sort) {
		List<AssetEntry> results = new ArrayList<AssetEntry>();
		try {

			List<CPCatalogEntry> cpCatalogEntries = getPurchasedProductsByAccount();
			List<CPAttachmentFileEntry> attachments = new ArrayList<CPAttachmentFileEntry>();
			
		//Get all the attachment files of the products purchased by the selected commerce account
			for (CPCatalogEntry cpCatalogEntry : cpCatalogEntries) {
				attachments.addAll(_cpAttachmentFileEntryLocalServivce.getCPAttachmentFileEntries(new Long(41805).longValue(),
						cpCatalogEntry.getCPDefinitionId(), 1, 0, 0, Integer.MAX_VALUE));
			}
       
		//Get the AssetEntry associated with each file
			for (CPAttachmentFileEntry cpAttachmentFileEntry : attachments) {
				//IF to avoid duplicates
				if (!results.contains(_assetEntryLocalService.getEntry(dlFileEntryClassName, cpAttachmentFileEntry.getFileEntryId())))
				results.add(
						_assetEntryLocalService.getEntry(dlFileEntryClassName, cpAttachmentFileEntry.getFileEntryId()));
			}
		} catch (PortalException e) {
			e.printStackTrace();
		}

		// Crappy pagination management;
		if (pagination.getEnd()>results.size()) return results;
		
		return results.subList(pagination.getStart(), pagination.getEnd()-1);
	}

	@Override
	public int getInfoListCount(InfoListProviderContext infoListProviderContext) {
		List<AssetEntry> results = new ArrayList<AssetEntry>();
		try {

			List<CPCatalogEntry> cpCatalogEntries = getPurchasedProductsByAccount();
			List<CPAttachmentFileEntry> attachments = new ArrayList<CPAttachmentFileEntry>();
			
		//Get all the attachment files of the products purchased by the selected commerce account
			for (CPCatalogEntry cpCatalogEntry : cpCatalogEntries) {
				attachments.addAll(_cpAttachmentFileEntryLocalServivce.getCPAttachmentFileEntries(new Long(41805).longValue(),
						cpCatalogEntry.getCPDefinitionId(), 1, 0, 0, Integer.MAX_VALUE));
			}
       
		//Get the AssetEntry associated with each file
			for (CPAttachmentFileEntry cpAttachmentFileEntry : attachments) {
				//IF to avoid duplicates
				if (!results.contains(_assetEntryLocalService.getEntry(dlFileEntryClassName, cpAttachmentFileEntry.getFileEntryId())))
				results.add(
						_assetEntryLocalService.getEntry(dlFileEntryClassName, cpAttachmentFileEntry.getFileEntryId()));
			}
		} catch (PortalException e) {
			e.printStackTrace();
		}

		return results.size();
	}

	@Override
	public String getLabel(Locale locale) {
		return "Documents related to purchased products";
	}

	private long getCommerceAccountId(HttpServletRequest httpServletRequest, CommerceContext commerceContext)
			throws PortalException {

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();
		long commerceAccountId = 0;

		if (commerceAccount != null) {
			commerceAccountId = commerceAccount.getCommerceAccountId();
		}

		return commerceAccountId;
	}
	
	
	private List<CPCatalogEntry> getPurchasedProductsByAccount() throws PortalException{

		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

		HttpServletRequest httpServletRequest = serviceContext.getRequest();

		ThemeDisplay themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);

		CommerceContext commerceContext = (CommerceContext) httpServletRequest
				.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);

			long commerceaccountId = getCommerceAccountId(httpServletRequest, commerceContext);
			//Company company = infoListProviderContext.getCompany();
			long companyId = _portal.getCompanyId(httpServletRequest);
			// The groupId of the orders is the ChannelId, and not the GroupId
			long groupId = commerceContext.getCommerceChannelGroupId();

			List<CPCatalogEntry> cpCatalogEntries = _commerceCustomService.getPurchasedProducts(companyId, groupId,
					commerceaccountId, themeDisplay.getLocale(), 0, Integer.MAX_VALUE);
			
			return cpCatalogEntries;
	}
	

	private static final Log _log = LogFactoryUtil.getLog(RelatedDocumentInfoListProvider.class);

	String dlFileEntryClassName = "com.liferay.document.library.kernel.model.DLFileEntry";

	@Reference
	private Portal _portal;

	@Reference
	AssetEntryLocalService _assetEntryLocalService;

	@Reference
	CPAttachmentFileEntryLocalService _cpAttachmentFileEntryLocalServivce;

	@Reference
	DLFileEntryLocalService _dlFileEntryLocalService;

	@Reference
	CommerceCustomService _commerceCustomService;

}