package de.factfinder.runner;

import org.apache.commons.codec.digest.DigestUtils;

import de.factfinder.wsclient.ws610.AuthenticationToken;
import de.factfinder.wsclient.ws610.tracking.TrackingInformation;

/**
 * This class manages the {@linkplain AuthenticationToken} , channel and the webservice url.
 */
public final class Settings {

	/**
	 * Base URL of the FACT-Finder environment.
	 */
	protected static final String		BASEURL				= "http://localhost:8080/FACT-Finder6.10";
	private static final String			CHANNEL				= "de";

	// authentication settings for requests
	private static final String			REQUEST_USER		= "user";
	private static final String			REQUEST_PASSWORD	= "userpw";
	private static final String			PREFIX				= "FACT-FINDER";
	private static final String			POSTFIX				= "FACT-FINDER";

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
		/*
		 * For demo purpose normal Hashed password technique is used.
		 */
		final String hashPassword = DigestUtils.md5Hex(REQUEST_PASSWORD);
		final long timeStamp = System.currentTimeMillis();
		authToken.setPassword(DigestUtils.md5Hex(PREFIX + timeStamp + hashPassword + POSTFIX));
		authToken.setTimestamp(Long.toString(timeStamp));
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
