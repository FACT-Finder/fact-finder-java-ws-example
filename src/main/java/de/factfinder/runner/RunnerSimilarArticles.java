package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.SimilarProductsPortTypeProxy;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.wsclient.ws611.AuthenticationToken;
import de.factfinder.wsclient.ws611.Record;
import de.factfinder.wsclient.ws611.similarresults.SimilarRecordsResult;

public class RunnerSimilarArticles {
	private static final Logger	LOG	= Logger.getLogger(RunnerSimilarArticles.class.getCanonicalName());

	public static void main(final String[] args) {
		final SimilarProductsPortTypeProxy proxy = new SimilarProductsPortTypeProxy(Settings.getUrl(WebServiceUrlType.SIMILAR_ARTICLES));
		final SearchResultInformationPrinter srip = new SearchResultInformationPrinter();

		try {
			final String channel = Settings.getChannel();
			final AuthenticationToken authToken = Settings.getAuthToken();
			final int maxArticles = 10;
			final String recordId = "1458";

			LOG.info("=== BEGIN SIMILAR ARTICLES (NORMAL)===");
			SimilarRecordsResult srr = proxy.getSimilarProducts(channel, recordId, false, maxArticles, authToken);
			for (final Record rec : srr.getRecords()) {
				srip.printRecord(rec, "");
			}
			LOG.info("=== END SIMILAR ARTICLES (NORMAL) ===");

			LOG.info("=== BEGIN SIMILAR ARTICLES (IDs ONLY)===");
			srr = proxy.getSimilarProducts(channel, recordId, true, maxArticles, authToken);
			for (final Record rec : srr.getRecords()) {
				srip.printRecord(rec, "");
			}
			LOG.info("=== END SIMILAR ARTICLES (IDs ONLY) ===");

		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}
}
