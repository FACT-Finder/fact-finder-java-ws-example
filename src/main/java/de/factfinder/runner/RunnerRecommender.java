package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.RecommenderPortTypeProxy;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.wsclient.ws611.AuthenticationToken;
import de.factfinder.wsclient.ws611.Record;

public class RunnerRecommender {
	private static final Logger	LOG					= Logger.getLogger(RunnerRecommender.class.getCanonicalName());
	private static final int	MAX_RETURN_RECORDS	= 10;

	public static void main(final String[] args) {
		sendRequest(Settings.getUrl(WebServiceUrlType.RECOMMENDER));
	}

	private static void sendRequest(final String endpoint) {
		final RecommenderPortTypeProxy proxy = new RecommenderPortTypeProxy(endpoint);
		final SearchResultInformationPrinter searchResultInfoPrinter = new SearchResultInformationPrinter();

		try {
			final String channel = Settings.getChannel();
			final AuthenticationToken token = Settings.getAuthToken();

			importDatabase(proxy, channel, token);

			final String productMatchOnFieldValue1 = "1892333";

			LOG.info("==== BEGIN RECOMMENDATION RESULT (NORMAL) ====");
			Record[] records = proxy.getRecommendation(MAX_RETURN_RECORDS, productMatchOnFieldValue1, channel, false, token).getResultRecords();
			printResult(searchResultInfoPrinter, records);
			LOG.info("==== END RECOMMENDATION RESULT (NORMAL) ====");

			LOG.info("==== BEGIN RECOMMENDATION RESULT (IDs ONLY) ====");
			// NOTE: when using the idsOnly-mode, the returned record-id is the value from the matchOnField which might not be the normal record-id.
			records = proxy.getRecommendation(MAX_RETURN_RECORDS, productMatchOnFieldValue1, channel, true, token).getResultRecords();
			printResult(searchResultInfoPrinter, records);
			LOG.info("==== END RECOMMENDATION RESULT (IDs ONLY) ====");

			LOG.info("==== BEGIN RECOMMENDATION RESULT (MULTI PRODUCTS; IDs ONLY) ====");
			final String productMatchOnFieldValue2 = "1715749";
			final String[] multipleProducts = {productMatchOnFieldValue1, productMatchOnFieldValue2};
			records = proxy.getRecommendation1(MAX_RETURN_RECORDS, multipleProducts, channel, true, token).getResultRecords();
			printResult(searchResultInfoPrinter, records);
			LOG.info("==== END RECOMMENDATION RESULT (MULTI PRODUCTS; IDs ONLY) ====");
		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}

	private static void printResult(final SearchResultInformationPrinter searchResultInfoPrinter, final Record[] records) {
		if (records != null && records.length > 0) {
			for (final Record rec : records) {
				searchResultInfoPrinter.printRecord(rec, "");
			}
		} else {
			LOG.info("No recommendations received.");
		}
	}

	private static void importDatabase(final RecommenderPortTypeProxy proxy, final String channel, final AuthenticationToken token) throws RemoteException {
		final boolean success = proxy.importData1(channel, true, token);
		LOG.info("RecommenderEngine database for channel '" + channel + "' has been imported. Status: " + success);
	}
}
