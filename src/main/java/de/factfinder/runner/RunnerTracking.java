package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws69.String2StringMapEntry;
import de.factfinder.adapters.wsclient.ws69.TrackingPortTypeProxy;

public class RunnerTracking {
	private static final Logger	LOG	= Logger.getLogger(RunnerTracking.class.getCanonicalName());

	public static void main(final String[] args) {
		sendRequest(Settings.getUrl(WebServiceUrlType.TRACKING));
	}

	private static void sendRequest(final String endpoint) {
		// Timeout.setTimeoutValue(TIMEOUT);
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			final String2StringMapEntry event = new String2StringMapEntry("event", "inspect");
			final String2StringMapEntry refKey = new String2StringMapEntry("sourceRefKey", "aoeuhtns123");
			final String2StringMapEntry channel = new String2StringMapEntry("channel", "de");
			final String2StringMapEntry id = new String2StringMapEntry("id", "1234-56");
			final String2StringMapEntry sid = new String2StringMapEntry("sid", "abc123def456ghi789");
			final String2StringMapEntry[] parameters = {event, refKey, channel, id, sid};
			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}
}
