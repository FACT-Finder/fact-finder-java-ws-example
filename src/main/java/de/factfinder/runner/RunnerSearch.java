package de.factfinder.runner;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws71.GetResult;
import de.factfinder.adapters.wsclient.ws71.GetSuggestions;
import de.factfinder.adapters.wsclient.ws71.SearchPortType;
import de.factfinder.runner.print.CampaignInformationPrinter;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws71.campaign.Campaign;
import de.factfinder.wsclient.ws71.search.FilterValue;
import de.factfinder.wsclient.ws71.search.FilterValueType;
import de.factfinder.wsclient.ws71.search.Params;
import de.factfinder.wsclient.ws71.search.Result;
import de.factfinder.wsclient.ws71.search.SearchControlParams;
import de.factfinder.wsclient.ws71.search.SearchResultStatus;
import de.factfinder.wsclient.ws71.suggest.ResultSuggestion;
import de.factfinder.wsclient.ws71.suggest.SuggestResult;

/**
 * This class demonstrates the usage of the FACT-Finder web service.
 * 
 * <p>
 * Please note: the package names are changed from those in the WSDL. By default, the package names start with {@code de.factfinder}. In this generated client
 * all package names start with {@code de.factfinder.wsclient}. Eclipse supports this mapping.
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
		params.setNoArticleNumberSearch(true);
		// setting empty filters, only needed if filters get set.

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

		// // For filtering the result
		// // Only show products in the category "DVD". The filter name is the name of a field in the search database.
		// final Filter filter = new Filter();
		// filter.setName("Kategorie");
		// filter.setValueList(Arrays.asList(filterValue("DVD")));
		// filter.setSubstring(Boolean.FALSE);
		// params.getFilters().add(filter);

		// // OR: only show products which are from manufacturer Atari or Vivendi
		// final Filter filter = new Filter();
		// filter.setName("Manufacturer");
		// filter.setValueList(Arrays.asList(filterValue("Adidas")));
		// filter.setSubstring(Boolean.FALSE);
		// params.getFilters().add(filter);

		sendRequest(endpoint, params, searchControlParams);
	}

	@SuppressWarnings("unused")
	private static FilterValue filterValue(final String value) {
		return filterValue(FilterValueType.AND, value);
	}

	private static FilterValue filterValue(final FilterValueType type, final String value) {
		final FilterValue result = new FilterValue();
		result.setExclude(false);
		result.setType(type);
		result.setValue(value);
		return result;
	}

	/**
	 * Sends a search request to the endpoint and prints the result.
	 *
	 * @param endPoint The web service URL.
	 * @param params The search parameters.
	 * @param searchControlParams the search control parameters.
	 */
	private static void sendRequest(final String endPoint, final Params params, final SearchControlParams searchControlParams) {
		final Service<SearchPortType> service = new Service<>(SearchPortType.class, endPoint);
		// enable gzip
		service.enableGZIP();

		final SearchPortType proxy = service.get();
		// Timeout.setTimeoutValue(TIMEOUT);

		final SearchResultInformationPrinter searchInfoPrinter = new SearchResultInformationPrinter();

		LOG.info("==== BEGIN SEARCH PARAMETERS SENT ====");
		searchInfoPrinter.printSearchParameters(params);
		LOG.info("==== END SEARCH PARAMETERS SENT ====\n");

		// Print the parameters which are going to be sent to the web service
		LOG.info("==== BEGIN SUGGEST ENTRIES ====");
		printSuggestEntries(proxy, params, searchControlParams);

		getRefKey(service);

		LOG.info("==== END SUGGEST ENTRIES ====");

		// Perform the search
		GetResult get = new GetResult();
		get.setIn0(params);
		get.setIn1(searchControlParams);
		get.setIn2(Settings.getAuthToken());
		get.setIn3(Settings.getTrackingInformation());
		final Result result = proxy.getResult(get).getOut();

		getRefKey(service);

		// The search result contains all matching campaigns, which we will process next.
		LOG.info("==== BEGIN CAMPAIGNS ====");
		final List<Campaign> campaigns = result.getCampaigns();
		final CampaignInformationPrinter campaignInfoPrinter = new CampaignInformationPrinter(searchInfoPrinter);
		campaignInfoPrinter.printCampaigns(campaigns);
		LOG.info("==== END CAMPAIGNS ====");

		// Check if there didn't occur any problems while searching. If that happens, you probably have to take a look at the log files of the search
		// application because "normal" errors result in an exception. If you don't host the search application yourself, ask Omikron to take a look at
		// the logs.
		if (result.getResultStatus() != SearchResultStatus.ERROR_OCCURED) {
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
	}

	private static String getRefKey(final Service service) {
		String refKeyValues = (String) service.getResponseContext("X-FF-RefKey");

		LOG.info("==== LATEST REF_KEY: " + refKeyValues + " ====");

		return refKeyValues;
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
	 * @throws RemoteException If the request fails.
	 */
	private static void printSuggestEntries(final SearchPortType proxy, final Params searchParams, final SearchControlParams searchControlParams) {
		// Iterate over the suggest search terms
		for (int i = 1; i <= searchParams.getQuery().length(); i++) {
			// Create the partial search term
			final String searchTerm = searchParams.getQuery().substring(0, i);

			final Params suggestParams = new Params();
			suggestParams.setChannel(searchParams.getChannel());
			suggestParams.setQuery(searchTerm);
			suggestParams.setResultsPerPage(Integer.MAX_VALUE);

			GetSuggestions get = new GetSuggestions();
			get.setIn0(suggestParams);
			get.setIn1(searchControlParams);
			get.setIn2(Settings.getAuthToken());

			final SuggestResult suggestions = proxy.getSuggestions(get).getOut();
			LOG.trace("Found " + suggestions.getSuggestions().size() + " suggest entries for search term [" + suggestParams.getQuery() + ']');
			for (final ResultSuggestion suggestion : suggestions.getSuggestions()) {
				// By default the list is limited to 10 entries. This value can be configured by Omikron.
				LOG.trace("search term=[" + suggestion.getName() + "], type=[" + suggestion.getType() + "], hit count=" + suggestion.getHitCount());
			}
		}
	}
}
