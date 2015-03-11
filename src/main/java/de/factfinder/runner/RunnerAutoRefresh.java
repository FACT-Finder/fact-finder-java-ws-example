package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.RefreshDatabasesServicePortTypeProxy;
import de.factfinder.wsclient.ws611.AuthenticationToken;

public class RunnerAutoRefresh {
	private static final Logger	LOG	= Logger.getLogger(RunnerAutoRefresh.class.getCanonicalName());

	public static void main(final String[] args) {
		sendRequest(Settings.getUrl(WebServiceUrlType.REFRESHDATABASES));
	}

	private static void sendRequest(final String endpoint) {
		final RefreshDatabasesServicePortTypeProxy proxy = new RefreshDatabasesServicePortTypeProxy(endpoint);

		try {
			final String channel = Settings.getChannel();
			final AuthenticationToken token = Settings.getAuthToken();
			proxy.refreshDatabase(channel, token);
			LOG.info("Search database of channel '" + channel + "' got refreshed!");

			proxy.refreshSuggestDatabase(channel, token);
			LOG.info("Suggest database of channel '" + channel + "' got refreshed!");

			proxy.refreshRecommenderDatabase(channel, token);
			LOG.info("RecommendationEngine database of channel '" + channel + "' got refreshed!");

			proxy.refreshAllDatabases(token);
			LOG.info("All databases have been refreshed!");

		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}
}
