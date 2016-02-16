package de.factfinder.runner;

import org.apache.commons.codec.digest.DigestUtils;

import de.factfinder.wsclient.ws71.AuthenticationToken;
import de.factfinder.wsclient.ws71.tracking.TrackingInformation;

/**
 * This class manages the {@linkplain AuthenticationToken} , channel and the webservice url.
 */
public final class Settings {

	/**
	 * Base URL of the FACT-Finder environment.
	 */
	private static final String			APPLICATION_URL		= "http://localhost:8080/FACT-Finder7.1";
	protected static final String		WEBSERVICE_BASE_URL	= APPLICATION_URL + "/webservice/ws71";
	private static final String			CHANNEL				= "de";

	// authentication settings for requests
	private static final String			REQUEST_USER		= "user";
	private static final String			REQUEST_PASSWORD	= "userpw";
	private static final String			PREFIX				= "FACT-FINDER";
	private static final String			POSTFIX				= "FACT-FINDER";
	private static final boolean		ADVANCED_MODE		= true;

	// tracking related settings, see documentation for meaning.
	private static AuthenticationToken	authToken			= null;
	private static final String			SITE				= "demoshop";
	private static final String			SESSION_ID			= "sid12345";

	private Settings() {
	}

	/**
	 * Returns the {@linkplain AuthenticationToken}.
	 * 
	 * @return a {@linkplain AuthenticationToken}.
	 */
	protected static AuthenticationToken getAuthToken() {
		if (authToken == null) {
			authToken = new AuthenticationToken();
			authToken.setUsername(REQUEST_USER);
		}

		final String hashPassword = DigestUtils.md5Hex(REQUEST_PASSWORD);
		if (ADVANCED_MODE) {
			final long timeStamp = System.currentTimeMillis();
			authToken.setPassword(DigestUtils.md5Hex(PREFIX + timeStamp + hashPassword + POSTFIX));
			authToken.setTimestamp(Long.toString(timeStamp));
		} else {
			authToken.setPassword(hashPassword);
		}

		return authToken;
	}

	protected static TrackingInformation getTrackingInformation() {
		final TrackingInformation userInformation = new TrackingInformation();
		userInformation.setSite(SITE);
		userInformation.setSessionID(SESSION_ID);
		return userInformation;
	}

	/**
	 * Returns the webservice url by reference to the type.
	 * 
	 * @param type indicating which url should be returned.
	 * @return a webservice url.
	 */
	protected static String getUrl(final WebServiceUrlType type) {
		return type.getUrl();
	}

	/**
	 * Returns the channel.
	 * 
	 * @return the channel
	 */
	protected static String getChannel() {
		return CHANNEL;
	}
}
