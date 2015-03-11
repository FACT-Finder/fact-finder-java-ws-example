package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws610.TagcloudPortTypeProxy;
import de.factfinder.wsclient.ws610.AuthenticationToken;
import de.factfinder.wsclient.ws610.search.Params;
import de.factfinder.wsclient.ws610.whatshot.TagCloud;
import de.factfinder.wsclient.ws610.whatshot.TagCloudEntry;

public class RunnerTagCloud {
	private static final Logger	LOG	= Logger.getLogger(RunnerTagCloud.class.getCanonicalName());

	/**
	 * Sends tagcloud requests and prints the tagcloud search terms.
	 * 
	 * @param proxy The service instance.
	 * @param searchParams The search parameters.
	 * @param token The authentication token.
	 * @throws RemoteException If the request fails.
	 */
	private static void printTagcloudEntries(final TagcloudPortTypeProxy proxy, final Params searchParams, final AuthenticationToken token)
			throws RemoteException {
		// Iterate over the suggest search terms
		for (int i = 1; i <= searchParams.getQuery().length(); i++) {
			// Create the partial search term
			final String searchTerm = searchParams.getQuery().substring(0, i);

			final Params suggestParams = new Params();
			suggestParams.setChannel(searchParams.getChannel());
			suggestParams.setQuery(searchTerm);
			suggestParams.setResultsPerPage(Integer.MAX_VALUE);
			final TagCloud tagCloud = proxy.getTagCloudEntries(Settings.getChannel(), 10, token);
			for (final TagCloudEntry entry : tagCloud.getEntries()) {
				LOG.trace("entry term=[" + entry.getSearchTerm() + "], weight=[" + entry.getWeight() + "]");
			}
		}
	}

	/**
	 * Runs the sample program.
	 * 
	 * <p>
	 * To run this code, fire up your search application and change the 'endpoint' variable to the URL of your application. If you don't have your own search
	 * application installed, you can use our demo shop, which is publicly accessible ({@code PUBLIC_URL}).
	 * </p>
	 * 
	 * @param args Unused.
	 */
	public static void main(final String[] args) {
		final String endpoint = Settings.getUrl(WebServiceUrlType.TAGCLOUD);
		final String channel = Settings.getChannel();

		// The term you want to search for
		final String searchTerm = "helm";

		// Channel and search term are the minimal parameters you should set. Actually you don't have to set anything. In that case the default channel is used
		// and the search term is empty.
		final Params params = new Params();
		params.setChannel(channel);
		params.setQuery(searchTerm);

		sendRequest(endpoint, params);
	}

	/**
	 * Sends a search request to the endpoint and prints the result.
	 * 
	 * @param endpoint The web service URL.
	 * @param params The search parameters.
	 */
	private static void sendRequest(final String endpoint, final Params params) {
		// Timeout.setTimeoutValue(TIMEOUT);
		final TagcloudPortTypeProxy proxy = new TagcloudPortTypeProxy();
		proxy.setEndpoint(endpoint);

		try {
			// Print the parameters which are going to be sent to the web service
			LOG.info("=== BEGIN TAGCLOUD ENTRIES ===");
			printTagcloudEntries(proxy, params, Settings.getAuthToken());
			LOG.info("=== END TAGCLOUD ENTRIES ===");

		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}

}
