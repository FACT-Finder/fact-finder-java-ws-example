package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws610.RecommenderPortTypeProxy;
import de.factfinder.wsclient.ws610.AuthenticationToken;
import de.factfinder.wsclient.ws610.Record;

public class RunnerRecommender {
	private static final Logger		LOG					= Logger.getLogger(RunnerRecommender.class.getCanonicalName());
	private static final String		PRODUCT_ID			= "233450";
	private static final int		MAX_RETURN_RECORDS	= 30;
	private static final String		CHANNEL				= "de";
	private static final boolean	IDS_ONLY			= false;

	public static void main(final String[] args) {
		sendRequest(Settings.getUrl(WebServiceUrlType.RECOMMENDER));
	}

	private static void sendRequest(final String endpoint) {
		final RecommenderPortTypeProxy proxy = new RecommenderPortTypeProxy(endpoint);

		try {
			final String channel = Settings.getChannel();
			final AuthenticationToken token = Settings.getAuthToken();
			final boolean success = proxy.importData1(channel, true, token);
			LOG.info("Recommender matrix has been built: " + success);
			final Record[] records = proxy.getRecommendation(MAX_RETURN_RECORDS, PRODUCT_ID, CHANNEL, IDS_ONLY, token).getResultRecords();
			if (records != null) {
				for (final Record rec : records) {
					printSearchRecord(rec);
				}
			}
		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}

	/**
	 * Prints a single search record.
	 * 
	 * @param record The record.
	 */
	private static void printSearchRecord(final Record record) {
		LOG.info("Record #" + record.getId());
	}
}
