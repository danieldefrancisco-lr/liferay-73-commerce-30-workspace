<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@taglib uri="http://liferay.com/tld/commerce" prefix="liferay-commerce" %><%@
taglib uri="http://liferay.com/tld/commerce-cart" prefix="liferay-commerce-cart" %><%@
taglib uri="http://liferay.com/tld/commerce-ui" prefix="commerce-ui" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %>

<%@ page import="commerce.demo.warehouse.web.model.CPWarehouse" %>
<%@ page import="com.liferay.commerce.constants.CommerceWebKeys" %><%@
page import="com.liferay.commerce.context.CommerceContext" %><%@
page import="com.liferay.commerce.model.CommerceOrder" %><%@
page import="com.liferay.commerce.inventory.model.CommerceInventoryWarehouse" %><%@
page import="com.liferay.commerce.price.list.model.CommercePriceEntry" %><%@
page import="com.liferay.commerce.price.list.service.CommercePriceEntryLocalServiceUtil" %><%@
page import="com.liferay.commerce.product.catalog.CPCatalogEntry" %><%@
page import="com.liferay.commerce.product.catalog.CPSku" %><%@
page import="com.liferay.commerce.product.content.constants.CPContentWebKeys" %><%@
page import="com.liferay.commerce.product.content.util.CPContentHelper" %><%@
page import="com.liferay.commerce.product.model.CPInstance" %><%@
page import="com.liferay.commerce.product.service.CPInstanceLocalServiceUtil" %><%@
page import="com.liferay.commerce.service.CommerceOrderLocalServiceUtil" %><%@
page import="com.liferay.commerce.inventory.service.CommerceInventoryWarehouseLocalServiceUtil" %><%@
page import="com.liferay.expando.kernel.model.ExpandoBridge" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portal.kernel.util.PortalUtil" %><%@
page import="com.liferay.commerce.product.constants.CPWebKeys" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="java.io.Serializable" %>
<%@ page import="java.util.List" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="java.util.Map" %><%@
page import="java.util.HashMap" %><%@
page import="com.liferay.portal.kernel.util.HashMapBuilder" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />