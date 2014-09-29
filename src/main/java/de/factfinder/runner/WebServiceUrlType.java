package de.factfinder.runner;

import static de.factfinder.runner.Settings.BASEURL;

/**
 * The possible urls for the webservice.
 */
public enum WebServiceUrlType {
	/** The search url. */
	SEARCH(BASEURL + "/webservice/ws69/Search"),
	/** The refresh databases url. */
	REFRESHDATABASES(BASEURL + "/webservice/ws69/RefreshDatabasesService"),
	/** The import url. */
	IMPORT(BASEURL + "/webservice/ws69/Import"),
	/** The recommender url. */
	RECOMMENDER(BASEURL + "/webservice/ws69/Recommender"),
	/** The tracking url. */
	TRACKING(BASEURL + "/webservice/ws69/Tracking"),
	/** The tagcloud url. */
	TAGCLOUD(BASEURL + "/webservice/ws69/Tagcloud"),
	/** The usermanagement url. */
	USERMANAGEMENT(BASEURL + "/webservice/ws69/UserManagement"),
	/** The campaign url. */
	CAMPAIGN(BASEURL + "/webservice/ws69/Campaign");

	private final String	url;

	WebServiceUrlType(final String url) {
		this.url = url;
	}

	protected String getUrl() {
		return url;
	}
}
