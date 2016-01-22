package de.factfinder.runner;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.CompareRecords;
import de.factfinder.adapters.wsclient.ws611.ProductComparisonPortType;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws611.AuthenticationToken;
import de.factfinder.wsclient.ws611.Record;
import de.factfinder.wsclient.ws611.compare.CompareAttribute;
import de.factfinder.wsclient.ws611.compare.CompareResult;

public class RunnerProductCompare {
	private static final Logger					LOG		= Logger.getLogger(RunnerProductCompare.class.getCanonicalName());

	public static void main(final String[] args) {
		final String endpoint = Settings.getUrl(WebServiceUrlType.PRODUCT_COMPARISON);
		final ProductComparisonPortType proxy = Service.get(ProductComparisonPortType.class, endpoint);
		final SearchResultInformationPrinter srip = new SearchResultInformationPrinter();

		final List<String> recordIds = Arrays.asList("278021", "278006", "271249");

		LOG.info("=== BEGIN PRODUCT COMPARISON (NORMAL)===");
		CompareResult cr = compareRecords(proxy, recordIds, false);
		printCompareResult(srip, cr);
		LOG.info("=== END PRODUCT COMPARISON (NORMAL) ===");

		LOG.info("=== BEGIN PRODUCT COMPARISON (IDs ONLY)===");
		// when requesting idsOnly, the records will be returned with its id and the field values of the compare attributes
		cr = compareRecords(proxy, recordIds, true);
		printCompareResult(srip, cr);
		LOG.info("=== END PRODUCT COMPARISON (IDs ONLY) ===");

	}

	private static CompareResult compareRecords(ProductComparisonPortType proxy, List<String> recordsIds, boolean idsOnly) {
		CompareRecords compare = new CompareRecords();
		compare.setIn0(Settings.getChannel());
		compare.setIn1(recordsIds);
		compare.setIn2(idsOnly);
		compare.setIn3(Settings.getAuthToken());
		return proxy.compareRecords(compare).getOut();
	}

	private static void printCompareResult(final SearchResultInformationPrinter srip, final CompareResult cr) {
		for (final CompareAttribute attr : cr.getAttributes()) {
			LOG.info("Attribute: '" + attr.getAttributeName() + "', Fieldname: '" + attr.getSourceField() + "', hasDifference: " + attr.isDifferent());
		}
		LOG.info("---");
		for (final Record rec : cr.getRecords()) {
			srip.printRecord(rec, "");
		}
	}
}
