package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.wsclient.ws69.AuthenticationToken;
import de.factfinder.wsclient.ws69.Record;
import de.factfinder.adapters.wsclient.ws69.RecommenderPortTypeProxy;
import de.factfinder.wsclient.ws69.tracking.UserInformation;

public class RunnerRecommender {
	private static final Logger				LOG					= Logger.getLogger(RunnerRecommender.class.getCanonicalName());
	private static final String				PRODUCT_ID			= "249602";
	private static final int				MAX_RETURN_RECORDS	= 30;
	private static final UserInformation	USER_INFORMATION	= null;
	private static final String				CHANNEL				= "de";
	private static final boolean			IDS_ONLY			= false;

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
			final Record[] records = proxy.getRecommendation(MAX_RETURN_RECORDS, PRODUCT_ID, null, USER_INFORMATION, CHANNEL, IDS_ONLY, token);
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
