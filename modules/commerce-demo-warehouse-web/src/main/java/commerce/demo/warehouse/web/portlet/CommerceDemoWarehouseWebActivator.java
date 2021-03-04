package commerce.demo.warehouse.web.portlet;

import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.expando.kernel.exception.DuplicateColumnNameException;
import com.liferay.expando.kernel.exception.DuplicateTableNameException;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalServiceUtil;
import com.liferay.expando.kernel.service.ExpandoTableLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.Portal;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import commerce.demo.warehouse.web.constants.CommerceDemoWarehouseWebPortletKeys;

@Component(
    immediate = true,
    service = CommerceDemoWarehouseWebActivator.class
)
public class CommerceDemoWarehouseWebActivator {
	
	@Activate
    void activate() throws Exception {

		_log.debug("Creating Custom Field priceListId for Commerce Warehouse ");
        setupExpando();
    }

    public String getDescription() {

        return this.getClass().getSimpleName();
    }

    protected void setupExpando() throws Exception {
		ExpandoTable table = null;
		ExpandoColumn column = null;
		long companyId = _portal.getCompanyIds()[0];
		_log.debug("In companyId: "+ companyId);

		try {
			table = ExpandoTableLocalServiceUtil.addTable(companyId,
					CommerceInventoryWarehouse.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME);
		}
		catch (DuplicateTableNameException dtne) {
			table = ExpandoTableLocalServiceUtil.getTable(companyId,
					CommerceInventoryWarehouse.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME);
			
		}

		String columnTypeSettings="display-type=input-field\r\n" + 
				" hidden=false\r\n" + 
				" index-type=0\r\n" + 
				" localize-field=false\r\n" + 
				" localize-field-name=false\r\n" + 
				" secret=false\r\n" + 
				" visible-with-update-permission=false\r\n" + 
				" width=0\r\n"; 
				
		try {
			column = ExpandoColumnLocalServiceUtil.addColumn(
				table.getTableId(), CommerceDemoWarehouseWebPortletKeys.PRICELISTID_CUSTOMFIELD,
				ExpandoColumnConstants.INTEGER);
			ExpandoColumnLocalServiceUtil.updateTypeSettings(column.getColumnId(), columnTypeSettings);
		}
		catch (DuplicateColumnNameException dcne) {
			column = ExpandoColumnLocalServiceUtil.getColumn(
					table.getTableId(), CommerceDemoWarehouseWebPortletKeys.PRICELISTID_CUSTOMFIELD);
			_log.debug("Custom Field " + CommerceDemoWarehouseWebPortletKeys.PRICELISTID_CUSTOMFIELD +"already exists");
		}
		
		_log.debug("Updating Permissions on Custom Field to grant VIEW permission to role USER");
		String[] actionIds = new String[] { ActionKeys.VIEW };
		Role userRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.USER);
		ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, ExpandoColumn.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(column.getColumnId()), userRole.getRoleId(), actionIds);
	}
    
    
    @Reference
	private Portal _portal;
    
    private static final Log _log = LogFactoryUtil.getLog(CommerceDemoWarehouseWebActivator.class);
    
}
