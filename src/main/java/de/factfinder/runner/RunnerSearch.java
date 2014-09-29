package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.axis.client.Stub;
import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws69.SearchPortTypeProxy;
import de.factfinder.runner.print.CampaignInformationPrinter;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.runner.util.GzipUtil;
import de.factfinder.wsclient.ws69.AuthenticationToken;
import de.factfinder.wsclient.ws69.campaign.Campaign;
import de.factfinder.wsclient.ws69.search.Params;
import de.factfinder.wsclient.ws69.search.Result;
import de.factfinder.wsclient.ws69.search.SearchControlParams;
import de.factfinder.wsclient.ws69.suggest.ResultSuggestion;

/**
 * This class demonstrates the usage of the FACT-Finder web service.
 * 
 * <p>
 * Please note: the package names are changed from those in the WSDL. By default, the package names start with {@code de.factfinder}. In this generated client
 * all package names start with {@code de.factfinder.wsclient}. Eclipse supports this mapping. The mapping can be found in
 * '/src/main/resources/namespace-package-mapping.properties'.
 * </p>
 * 
 * <p>
 * By running the main method of this class a web service request is sent to the URL you set in the variable {@code endpoint}. The response is written to the
 * console by default. You may modify this behaviour by changing the file {@code src/main/resources/log4j.xml}.
 * </p>
 * 
 * <p>
 * This client is designed to run without many dependencies. We decided to add log4j to the dependencies because it lets you control the output pattern in a
 * nice way. If you don't want to use log4j, simply replace all occurrences of {@code log.info} by {@code System.out.println}.
 * </p>
 */
public class RunnerSearch {
	private static final Logger	LOG	= Logger.getLogger(RunnerSearch.class.getCanonicalName());

	// See de.factfinder.runner.util.GzipUtil for more information about this commented out code
	// static {
	// AxisProperties.setProperty(EngineConfigurationFactory.SYSTEM_PROPERTY_NAME, CompressedEngineConfigurationFactory.class.getName());
	// }

	// Timeout setting
	// private static final Integer TIMEOUT = new Integer(15000);

	/**
	 * Runs the sample program.
	 * 
	 * <p>
	 * To run this code, fire up your search application and be sure that {@link WebServiceUrlType#SEARCH} matches your Url. If you don't have your own search
	 * application installed, you can use our demo shop, which is publicly accessible.
	 * </p>
	 * 
	 * @param args Unused.
	 */
	public static void main(final String[] args) {
		final String endpoint = Settings.getUrl(WebServiceUrlType.SEARCH);
		final String channel = Settings.getChannel();

		// The term you want to search for
		final String searchTerm = "bmx";

		// Channel and search term are the minimal parameters you should set. Actually you don't have to set anything. In that case the default channel is used
		// and the search term is empty.
		final Params params = new Params();
		params.setChannel(channel);
		params.setQuery(searchTerm);

		// we can set here search control parameters e.g. deactivate the asn.
		final SearchControlParams searchControlParams = new SearchControlParams();
		// if you want to disable the cache set this to true
		searchControlParams.setDisableCache(true);
		// if your using the advisor campaign type and want to receive the complete advisor tree set this to true
		searchControlParams.setGenerateAdvisorTree(true);
		// if you want to minimize the response size set this to true. This will only return the id's of the records and not the whole record.
		searchControlParams.setIdsOnly(false);
		// if you want the asn to be generated set this to true.
		searchControlParams.setUseAsn(true);
		// if you want to use the campaign manager set this to true.
		searchControlParams.setUseCampaigns(true);
		// if true a list of found words per record will be generated.
		searchControlParams.setUseFoundWords(true);
		// only relevant if your using SEO. If true this will return keywords related to each record.
		searchControlParams.setUseKeywords(true);

		// We want to filter the result.
		// Filter[] filters = new Filter[1];
		// Only show products in the category "DVD". The filter name is the name of a field in the search database.
		// filters[0] = new Filter("Kategorie", false, "DVD", null);
		// params.setFilters(filters);

		// FilterValue[] manufacturers = new FilterValue[] {new FilterValue(FilterValueType.and, "Atari"), new FilterValue(FilterValueType.and, "Vivendi")};
		// filters[0] = new Filter("Hersteller", manufacturers);

		sendRequest(endpoint, params, searchControlParams);
	}

	/**
	 * Sends a search request to the endpoint and prints the result.
	 * 
	 * @param endpoint The web service URL.
	 * @param params The search parameters.
	 * @param searchControlParams the search control parameters.
	 */
	private static void sendRequest(final String endpoint, final Params params, final SearchControlParams searchControlParams) {
		// Timeout.setTimeoutValue(TIMEOUT);
		final SearchPortTypeProxy proxy = new SearchPortTypeProxy();
		proxy.setEndpoint(endpoint);
		// enable gzip response support
		final Stub stub = (Stub) proxy.getSearchPortType();
		GzipUtil.setGzip(stub);

		final SearchResultInformationPrinter searchInfoPrinter = new SearchResultInformationPrinter();

		LOG.info("==== BEGIN SEARCH PARAMETERS SENT ====");
		searchInfoPrinter.printSearchParameters(params);
		LOG.info("==== END SEARCH PARAMETERS SENT ====\n");

		try {
			final AuthenticationToken token = Settings.getAuthToken();

			// Print the parameters which are going to be sent to the web service
			LOG.info("==== BEGIN SUGGEST ENTRIES ====");
			printSuggestEntries(proxy, params, searchControlParams, token);
			getRefKey(stub);

			LOG.info("==== END SUGGEST ENTRIES ====");

			// Perform the search
			final Result result = proxy.getResult1(params, searchControlParams, token, Settings.getUserInformation());
			getRefKey(stub);

			// The search result contains all matching campaigns, which we will process next.
			LOG.info("==== BEGIN CAMPAIGNS ====");
			final Campaign[] campaigns = result.getCampaigns();
			final CampaignInformationPrinter campaignInfoPrinter = new CampaignInformationPrinter(searchInfoPrinter);
			campaignInfoPrinter.printCampaigns(campaigns);
			LOG.info("==== END CAMPAIGNS ====");

			// Check if there didn't occur any problems while searching. If that happens, you probably have to take a look at the log files of the search
			// application because "normal" errors result in an exception. If you don't host the search application yourself, ask Omikron to take a look at
			// the logs.
			if (!"errorOccured".equals(result.getResultStatus().getValue())) {
				// After searching the search parameters which weren't set before are initialized with default values. I.e. if you don't specify a channel, it
				// will be set with the default channel. Which of your channels is the default channel can be configured by Omikron.
				LOG.info("==== BEGIN SEARCH PARAMETERS RETURNED ====");
				searchInfoPrinter.printSearchParameters(result.getSearchParams());
				LOG.info("==== END SEARCH PARAMETERS RETURNED ====\n");

				// Show the found records of the current page. If you don't want to request the result page by page you can set the "results per page" in the
				// search parameters to a high value like Integer.MAX_VALUE. Please notice that this can lead to huge responses with up to several MB per
				// request. Consider the performance drawback.
				LOG.info("==== BEGIN SEARCH RESULT ====");
				searchInfoPrinter.printSearchResult(result);
				LOG.info("==== END SEARCH RESULT ====\n");

				LOG.info("==== BEGIN RESULT GROUPS ====");
				searchInfoPrinter.printAfterSearchNavigation(result);
				LOG.info("==== END RESULT GROUPS ====");

			} else {
				LOG.info("An unknown error occurred while searching. Please check the logs of the search application.");
			}
		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}

	private static String getRefKey(final Stub stub) {
		String refKey = "";
		final String[] refKeyValues = stub._getCall().getResponseMessage().getMimeHeaders().getHeader("X-FF-RefKey");
		if (refKeyValues != null && refKeyValues.length > 0) {
			refKey = refKeyValues[0];
			LOG.info("==== LATEST REF_KEY: " + refKey + " ====");
		}
		return refKey;
	}

	/**
	 * Sends suggest requests and prints the suggested search terms.
	 * 
	 * <p>
	 * This method simulates a user typing in a search term. For each additional character a request is sent and the suggested search terms are printed. If the
	 * search terms reads {@code ring}, this method sends requests for {@code r}, {@code ri}, {@code rin}, and {@code ring} to simulate user input.
	 * </p>
	 * 
	 * @param proxy The service instance.
	 * @param searchParams The search parameters.
	 * @param searchControlParams the search control parameters.
	 * @param token The authentication token.
	 * @throws RemoteException If the request fails.
	 */
	private static void printSuggestEntries(final SearchPortTypeProxy proxy, final Params searchParams, final SearchControlParams searchControlParams,
			final AuthenticationToken token) throws RemoteException {
		// Iterate over the suggest search terms
		for (int i = 1; i <= searchParams.getQuery().length(); i++) {
			// Create the partial search term
			final String searchTerm = searchParams.getQuery().substring(0, i);

			final Params suggestParams = new Params();
			suggestParams.setChannel(searchParams.getChannel());
			suggestParams.setQuery(searchTerm);
			suggestParams.setResultsPerPage(Integer.MAX_VALUE);

			final ResultSuggestion[] suggestions = proxy.getSuggestions1(suggestParams, searchControlParams, token, Settings.getUserInformation());

			LOG.trace("Found " + suggestions.length + " suggest entries for search term [" + suggestParams.getQuery() + ']');
			for (final ResultSuggestion suggestion : suggestions) {
				// By default the list is limited to 10 entries. This value can be configured by Omikron.
				LOG.trace("search term=[" + suggestion.getName() + "], type=[" + suggestion.getType() + "], hit count=" + suggestion.getHitCount());
			}
		}
	}
}
