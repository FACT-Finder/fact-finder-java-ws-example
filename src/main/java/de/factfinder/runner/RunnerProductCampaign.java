package de.factfinder.runner;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws71.CampaignPortType;
import de.factfinder.adapters.wsclient.ws71.GetProductCampaigns;
import de.factfinder.adapters.wsclient.ws71.GetShoppingCartCampaigns;
import de.factfinder.runner.print.CampaignInformationPrinter;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws71.AuthenticationToken;
import de.factfinder.wsclient.ws71.campaign.Campaign;

/**
 * This is a example of how to use the Web-Services to access product campaigns.
 */
public final class RunnerProductCampaign {
	private static final String					PRODUCT_NUMBER						= "249602";
	private static final List<String>			SHOPPING_CART_PRODUCT_NUMBER_LIST	= Arrays.asList("232177", "249602");
	private static final boolean				IDS_ONLY							= false;
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
		final String endpoint = Settings.getUrl(WebServiceUrlType.CAMPAIGN);
		final CampaignPortType proxy = Service.get(CampaignPortType.class, endpoint);
		final SearchResultInformationPrinter searchResultInfoPrinter = new SearchResultInformationPrinter();
		final CampaignInformationPrinter campaignInfoPrinter = new CampaignInformationPrinter(searchResultInfoPrinter);

		printProductCampaigns(campaignInfoPrinter, proxy);
		printShoppingCartCampaigns(campaignInfoPrinter, proxy);

	}

	private static void printProductCampaigns(final CampaignInformationPrinter campaignInfoPrinter, final CampaignPortType proxy) {
		LOG.info("Product campaigns");
		GetProductCampaigns get = new GetProductCampaigns();
		get.setIn0(Settings.getChannel());
		get.setIn1(PRODUCT_NUMBER);
		get.setIn2(IDS_ONLY);
		get.setIn3(Settings.getAuthToken());
		final List<Campaign> campaigns = proxy.getProductCampaigns(get).getOut();
		campaignInfoPrinter.printCampaigns(campaigns);
	}

	private static void printShoppingCartCampaigns(final CampaignInformationPrinter campaignInfoPrinter, final CampaignPortType proxy) {
		LOG.info("Shopping cart campaigns");
		GetShoppingCartCampaigns get = new GetShoppingCartCampaigns();
		get.setIn0(Settings.getChannel());
		get.setIn1(SHOPPING_CART_PRODUCT_NUMBER_LIST);
		get.setIn2(IDS_ONLY);
		get.setIn3(Settings.getAuthToken());
		final List<Campaign> campaigns = proxy.getShoppingCartCampaigns(get).getOut();
		campaignInfoPrinter.printCampaigns(campaigns);
	}

}
