package de.factfinder.runner;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.RefreshAllDatabases;
import de.factfinder.adapters.wsclient.ws611.RefreshDatabase;
import de.factfinder.adapters.wsclient.ws611.RefreshDatabasesServicePortType;
import de.factfinder.adapters.wsclient.ws611.RefreshRecommenderDatabase;
import de.factfinder.adapters.wsclient.ws611.RefreshSuggestDatabase;
import de.factfinder.runner.util.Service;

public class RunnerAutoRefresh {
	private static final Logger	LOG		= Logger.getLogger(RunnerAutoRefresh.class.getCanonicalName());
	private static final String	CHANNEL	= Settings.getChannel();

	public static void main(final String[] args) {
		final RefreshDatabasesServicePortType proxy = Service.get(RefreshDatabasesServicePortType.class, WebServiceUrlType.REFRESHDATABASES.getUrl());

		RefreshDatabase refreshDatabase = new RefreshDatabase();
		refreshDatabase.setIn0(CHANNEL);
		refreshDatabase.setIn1(Settings.getAuthToken());
		proxy.refreshDatabase(refreshDatabase);
		LOG.info("Search database of channel '" + CHANNEL + "' got refreshed!");

		RefreshSuggestDatabase refreshSuggestDatabase = new RefreshSuggestDatabase();
		refreshSuggestDatabase.setIn0(CHANNEL);
		refreshSuggestDatabase.setIn1(Settings.getAuthToken());
		proxy.refreshSuggestDatabase(refreshSuggestDatabase);
		LOG.info("Suggest database of channel '" + CHANNEL + "' got refreshed!");

		RefreshRecommenderDatabase refreshRecommenderDatabase = new RefreshRecommenderDatabase();
		refreshRecommenderDatabase.setIn0(CHANNEL);
		refreshRecommenderDatabase.setIn1(Settings.getAuthToken());
		proxy.refreshRecommenderDatabase(refreshRecommenderDatabase);
		LOG.info("RecommendationEngine database of channel '" + CHANNEL + "' got refreshed!");

		RefreshAllDatabases refreshAllDatabases = new RefreshAllDatabases();
		refreshAllDatabases.setIn0(Settings.getAuthToken());
		proxy.refreshAllDatabases(refreshAllDatabases);
		LOG.info("All databases have been refreshed!");
	}
}
