package de.factfinder.runner;

import static de.factfinder.runner.Settings.WEBSERVICE_BASE_URL;

/**
 * The possible urls for the webservice.
 */
public enum WebServiceUrlType {
	/** The search url. */
	SEARCH(WEBSERVICE_BASE_URL + "/Search"),
	/** The refresh databases url. */
	REFRESHDATABASES(WEBSERVICE_BASE_URL + "/RefreshDatabasesService"),
	/** The import url. */
	IMPORT(WEBSERVICE_BASE_URL + "/Import"),
	/** The recommender url. */
	RECOMMENDER(WEBSERVICE_BASE_URL + "/Recommender"),
	/** The tracking url. */
	TRACKING(WEBSERVICE_BASE_URL + "/Tracking"),
	/** The tagcloud url. */
	TAGCLOUD(WEBSERVICE_BASE_URL + "/Tagcloud"),
	/** The usermanagement url. */
	USERMANAGEMENT(WEBSERVICE_BASE_URL + "/UserManagement"),
	/** The campaign url. */
	CAMPAIGN(WEBSERVICE_BASE_URL + "/Campaign"),
	/** The similar articles url. */
	SIMILAR_ARTICLES(WEBSERVICE_BASE_URL + "/SimilarProducts"),
	/** The endpoint for product comparison. */
	PRODUCT_COMPARISON(WEBSERVICE_BASE_URL + "/ProductComparison");

	private final String	url;

	WebServiceUrlType(final String url) {
		this.url = url;
	}

	protected String getUrl() {
		return url;
	}
}
