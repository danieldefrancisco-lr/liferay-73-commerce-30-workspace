
package commerce.demo.additional.info.checkout.step;

import com.liferay.commerce.util.CommerceCheckoutStep;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;

import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The checkout step name must be a unique value so that Liferay Commerce can
 * distinguish our checkout step from existing checkout steps. The
 * commerce.checkout.step.order value indicates how far into the checkout
 * process the checkout step will appear. For example, the shipping method
 * checkout step has a value of 20. Giving our checkout step a value of 21
 * ensures that it will appear immediately after the shipping method step.
 */
@Component(immediate = true, property = {
	"commerce.checkout.step.name=" + AdditionalInfoCheckoutStep.NAME,
	"commerce.checkout.step.order:Integer=1"
}, service = CommerceCheckoutStep.class)
public class AdditionalInfoCheckoutStep implements CommerceCheckoutStep {

	public static final String NAME = "Additional Order Info";

	@Override
	public String getLabel(Locale locale) {

		return NAME;
	}

	@Override
	public String getName() {

		return NAME;
	}

	/**
	 * Set active is order has a Portability order item.
	 */
	@Override
	public boolean isActive(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse)
		throws Exception {

//		CommerceOrder commerceOrder =
//			(CommerceOrder) httpServletRequest.getAttribute(
//				CommerceCheckoutWebKeys.COMMERCE_ORDER);
//
//		List<CommerceOrderItem> commerceOrderItems =
//			commerceOrder.getCommerceOrderItems();
//
//		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
//			if (commerceOrderItem.getCPDefinition().getName().contains("Portability")) {
//				return true;
//			}
//		}
		return true;
	}

	@Override
	public boolean isOrder() {

		return false;
	}

	@Override
	public boolean isSennaDisabled() {

		return false;
	}

	@Override
	public boolean isVisible(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse)
		throws Exception {

		return isActive(httpServletRequest, httpServletResponse);
	}

	@Override
	public void processAction(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {
		
		// Checkout step logic

	}

	/**
	 * Use a JSPRenderer to render the JSP for our checkout step.
	 */
	@Override
	public void render(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse)
		throws Exception {

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/checkout_step/view.jsp");
	}

	@Override
	public boolean showControls(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		return true;
	}

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference(target = "(osgi.web.symbolicname=commerce.demo.additional.info.checkout.step)")
	private ServletContext _servletContext;

}
