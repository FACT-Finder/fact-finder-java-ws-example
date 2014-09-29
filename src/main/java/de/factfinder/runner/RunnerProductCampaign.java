package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws610.CampaignPortTypeProxy;
import de.factfinder.runner.print.CampaignInformationPrinter;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.wsclient.ws610.AuthenticationToken;
import de.factfinder.wsclient.ws610.campaign.Campaign;

/**
 * This is a example of how to use the Web-Services to access product campaigns.
 */
public final class RunnerProductCampaign {
	private static final String					ENDPOINT							= Settings.getUrl(WebServiceUrlType.CAMPAIGN);
	private static final String					CHANNEL								= Settings.getChannel();
	private static final String					PRODUCT_NUMBER						= "249602";
	private static final String[]				SHOPPING_CART_PRODUCT_NUMBER_LIST	= {"232177", "249602"};
	private static final boolean				IDS_ONLY							= false;
	private static final AuthenticationToken	TOKEN								= Settings.getAuthToken();
	private static final Logger					LOG									= Logger.getLogger(RunnerProductCampaign.class);

	/**
	 * Private constructor.
	 */
	private RunnerProductCampaign() {
	}

	/**
	 * Executes the example. Please check the correctness of the constants within this class.
	 * 
	 * @param args not used.
	 */
	public static void main(final String[] args) {
		final CampaignPortTypeProxy proxy = new CampaignPortTypeProxy(ENDPOINT);
		final SearchResultInformationPrinter searchResultInfoPrinter = new SearchResultInformationPrinter();
		final CampaignInformationPrinter campaignInfoPrinter = new CampaignInformationPrinter(searchResultInfoPrinter);
		try {
			printProductCampaigns(campaignInfoPrinter, proxy);
			printShoppingCartCampaigns(campaignInfoPrinter, proxy);
		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}

	private static void printProductCampaigns(final CampaignInformationPrinter campaignInfoPrinter, final CampaignPortTypeProxy proxy) throws RemoteException {
		LOG.info("Product campaigns");
		final Campaign[] campaigns = proxy.getProductCampaigns(CHANNEL, PRODUCT_NUMBER, IDS_ONLY, TOKEN);
		campaignInfoPrinter.printCampaigns(campaigns);
	}

	private static void printShoppingCartCampaigns(final CampaignInformationPrinter campaignInfoPrinter, final CampaignPortTypeProxy proxy)
			throws RemoteException {
		LOG.info("Shopping cart campaigns");
		final Campaign[] campaigns = proxy.getShoppingCartCampaigns(CHANNEL, SHOPPING_CART_PRODUCT_NUMBER_LIST, IDS_ONLY, TOKEN);
		campaignInfoPrinter.printCampaigns(campaigns);
	}

}
