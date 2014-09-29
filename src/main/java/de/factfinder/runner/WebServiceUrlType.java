package de.factfinder.runner;

import static de.factfinder.runner.Settings.BASEURL;

/**
 * The possible urls for the webservice.
 */
public enum WebServiceUrlType {
	/** The search url. */
	SEARCH(BASEURL + "/webservice/ws610/Search"),
	/** The refresh databases url. */
	REFRESHDATABASES(BASEURL + "/webservice/ws610/RefreshDatabasesService"),
	/** The import url. */
	IMPORT(BASEURL + "/webservice/ws610/Import"),
	/** The recommender url. */
	RECOMMENDER(BASEURL + "/webservice/ws610/Recommender"),
	/** The tracking url. */
	TRACKING(BASEURL + "/webservice/ws610/Tracking"),
	/** The tagcloud url. */
	TAGCLOUD(BASEURL + "/webservice/ws610/Tagcloud"),
	/** The usermanagement url. */
	USERMANAGEMENT(BASEURL + "/webservice/ws610/UserManagement"),
	/** The campaign url. */
	CAMPAIGN(BASEURL + "/webservice/ws610/Campaign");

	private final String	url;

	WebServiceUrlType(final String url) {
		this.url = url;
	}

	protected String getUrl() {
		return url;
	}
}
