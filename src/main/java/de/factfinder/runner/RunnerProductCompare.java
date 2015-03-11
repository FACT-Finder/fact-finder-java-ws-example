package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.ProductComparisonPortTypeProxy;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.wsclient.ws611.AuthenticationToken;
import de.factfinder.wsclient.ws611.Record;
import de.factfinder.wsclient.ws611.compare.CompareAttribute;
import de.factfinder.wsclient.ws611.compare.CompareResult;

public class RunnerProductCompare {
	private static final Logger	LOG	= Logger.getLogger(RunnerProductCompare.class.getCanonicalName());

	public static void main(final String[] args) {
		final ProductComparisonPortTypeProxy proxy = new ProductComparisonPortTypeProxy(Settings.getUrl(WebServiceUrlType.PRODUCT_COMPARISON));
		final SearchResultInformationPrinter srip = new SearchResultInformationPrinter();

		try {
			final String channel = Settings.getChannel();
			final AuthenticationToken authToken = Settings.getAuthToken();
			final String[] recordIds = {"278021", "278006", "271249"};

			LOG.info("=== BEGIN PRODUCT COMPARISON (NORMAL)===");
			CompareResult cr = proxy.compareRecords(channel, recordIds, false, authToken);
			printCompareResult(srip, cr);
			LOG.info("=== END PRODUCT COMPARISON (NORMAL) ===");

			LOG.info("=== BEGIN PRODUCT COMPARISON (IDs ONLY)===");
			// when requesting idsOnly, the records will be returned with its id and the field values of the compare attributes
			cr = proxy.compareRecords(channel, recordIds, true, authToken);
			printCompareResult(srip, cr);
			LOG.info("=== END PRODUCT COMPARISON (IDs ONLY) ===");

		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}

	private static void printCompareResult(final SearchResultInformationPrinter srip, final CompareResult cr) {
		for (final CompareAttribute attr : cr.getAttributes()) {
			LOG.info("Attribute: '" + attr.getAttributeName() + "', Fieldname: '" + attr.getSourceField() + "', hasDifference: " + attr.getDifferent());
		}
		LOG.info("---");
		for (final Record rec : cr.getRecords()) {
			srip.printRecord(rec, "");
		}
	}
}
