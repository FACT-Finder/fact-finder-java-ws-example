package de.factfinder.runner.print;

import java.util.List;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.String2StringMapEntry;
import de.factfinder.wsclient.ws611.Record;
import de.factfinder.wsclient.ws611.search.Filter;
import de.factfinder.wsclient.ws611.search.FilterValue;
import de.factfinder.wsclient.ws611.search.Group;
import de.factfinder.wsclient.ws611.search.GroupElement;
import de.factfinder.wsclient.ws611.search.Params;
import de.factfinder.wsclient.ws611.search.Result;
import de.factfinder.wsclient.ws611.search.SearchRecord;
import de.factfinder.wsclient.ws611.search.SortItem;

/**
 * Prints information about the search result.
 */
public final class SearchResultInformationPrinter {
	private final static Logger	LOG	= Logger.getLogger(SearchResultInformationPrinter.class);

	/**
	 * Prints the search parameters.
	 *
	 * @param params The search parameters.
	 */
	public void printSearchParameters(final Params params) {
		LOG.info("Channel: [" + params.getChannel() + "]");

		LOG.info("Query: [" + params.getQuery() + "]");

		LOG.info("Article number: [" + bool2Str(params.isArticleNumber()) + "]");

		LOG.info("No article number search: [" + bool2Str(params.isNoArticleNumberSearch())
				+ "] (if the query looks like an article number but FACT-Finder should perform a normal search)");

		LOG.info("Result page: [" + params.getPage() + "] (if there are more hits than set in 'records per page', "
				+ "the result is split into several pages. By setting this parameter you can get the records for a specific page.)");

		LOG.info("Records per page: [" + params.getResultsPerPage() + "]");

		LOG.info("Search field: [" + params.getSearchField() + "] (by default FACT-Finder searches in all fields "
				+ "in the database but you can set this parameter to let it search only in the specified field)");

		// Print sort options
		if (params.getSortsList() != null) {
			final StringBuilder msg = new StringBuilder("Sort options:");
			for (final SortItem item : params.getSortsList()) {
				msg.append("\n\tname=[").append(item.getName()).append("], order=[").append(item.getOrder());
				if (item.isSelected()) {
					msg.append(" (selected)");
				}
			}
			LOG.info(msg.toString());
		}

		// Print filters
		if (params.getFilters() != null) {
			final StringBuilder msg = new StringBuilder("Search filters:");
			for (final Filter filter : params.getFilters()) {
				msg.append("\n\tname=[").append(filter.getName()).append("]");

				// If this filter is set to a single value: manufacturer="Atari"
				if (filter.getValueList() != null) {
					msg.append(", value=[").append(filter.getValueList()).append("]");
				}

				// If the filter is set to several values: manufacturer="Atari" or "Apple"
				if (filter.getValueList() != null) {
					msg.append(", multiple values:");
					for (int i = 0; i < filter.getValueList().size(); i++) {
						final FilterValue value = filter.getValueList().get(i);
						if (i > 0) {
							msg.append(" ").append(value.getType());
						}
						msg.append(" ").append(value.getValue());
					}
				}
			}
			LOG.info(msg.toString());
		}
	}

	/**
	 * Prints a single search record.
	 *
	 * @param record The record.
	 */
	public void printSearchRecord(final SearchRecord record) {
		final StringBuilder recordString = printString2StringMap(record.getRecord());
		LOG.info("Record #" + record.getPosition() + ": " + recordString.toString());
	}

	/**
	 * Prints a single search record.
	 *
	 * @param record The record.
	 * @param prefix text which will be appended in front.
	 */
	public void printRecord(final Record record, final String prefix) {
		final StringBuilder recordString = printString2StringMap(record.getRecord());
		LOG.info(prefix + "Record: id=" + record.getId() + "; content:" + recordString.toString());
	}

	/**
	 * Prints the search result.
	 *
	 * @param result The search result.
	 */
	public void printSearchResult(final Result result) {
		LOG.info("Status: [" + result.getResultStatus() + "] (one of \'RESULTS_FOUND\', \'NOTHING_FOUND\', \'ERROR_OCCURED\')");
		LOG.info("Hit count: " + result.getResultCount());

		// This loop doesn't get all records. It only returns those which are present on the current page. The number of results per page can be set in the
		// search parameters
		LOG.info("Hits on page #" + result.getPaging().getCurrentPage());
		for (final SearchRecord record : result.getRecords()) {
			printSearchRecord(record);
		}
	}

	/**
	 * Prints the After Search Navigation (ASN).
	 *
	 * @param result The search result.
	 */
	public void printAfterSearchNavigation(final Result result) {
		LOG.info("Number of groups: " + result.getGroups().size());
		for (final Group group : result.getGroups()) {
			String groupMsg = "The group [" + group.getName() + "] contains " + group.getElements().size() + " elements and is ";
			if (group.getSelectedElements().size() == 0) {
				groupMsg += "not ";
			}
			groupMsg += "selected";
			LOG.info(groupMsg);

			String unit = group.getUnit();
			if (!unit.isEmpty()) {
				unit = " " + unit;
			}
			// Print the group elements and mark them as selected if applicable
			for (final GroupElement element : group.getElements()) {
				String elementMsg = "      " + element.getName() + unit + " (" + element.getRecordCount() + ")";
				if (element.isSelected()) {
					elementMsg += " <= selected";
				}
				LOG.info(elementMsg);
			}
		}
	}

	private StringBuilder printString2StringMap(final List<String2StringMapEntry> string2StringMap) {
		final StringBuilder recordString = new StringBuilder();
		for (final String2StringMapEntry entry : string2StringMap) {
			if (recordString.length() > 0) {
				recordString.append(", ");
			}
			recordString.append(entry.getKey()).append("=[").append(entry.getValue()).append("]");
		}
		return recordString;
	}

	private String bool2Str(final Boolean value) {
		return value == null ? "false" : value.toString();
	}
}
