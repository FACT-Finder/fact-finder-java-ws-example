package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws610.RefreshDatabasesServicePortTypeProxy;
import de.factfinder.wsclient.ws610.AuthenticationToken;

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
			LOG.info("DB " + channel + " refreshed!");
			proxy.refreshAllDatabases(token);
			LOG.info("All DBs refreshed!");

		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}
}
