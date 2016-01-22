package de.factfinder.runner;

import static de.factfinder.runner.util.Helper.newEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.DeleteRecord;
import de.factfinder.adapters.wsclient.ws611.ImportPortType;
import de.factfinder.adapters.wsclient.ws611.InsertRecord;
import de.factfinder.adapters.wsclient.ws611.StartImport;
import de.factfinder.adapters.wsclient.ws611.StartSuggestImport;
import de.factfinder.adapters.wsclient.ws611.String2StringMapEntry;
import de.factfinder.adapters.wsclient.ws611.UpdateRecord;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws611.AuthenticationToken;
import de.factfinder.wsclient.ws611.indexer.ImportRecord;

public class RunnerImport {
	private static final Logger					LOG		= Logger.getLogger(RunnerImport.class.getCanonicalName());
	private static final String					CHANNEL	= Settings.getChannel();
	private static final AuthenticationToken	TOKEN	= Settings.getAuthToken();

	public static void main(final String[] args) {
		final String endpoint = Settings.getUrl(WebServiceUrlType.IMPORT);
		final ImportPortType proxy = Service.get(ImportPortType.class, endpoint);

		ImportRecord record = new ImportRecord();

		LOG.info("Insert record");
		record.setId("123");
		record.setRecord(getRecordForInsertion());
		InsertRecord insert = new InsertRecord();
		insert.setIn0(record);
		insert.setIn1(CHANNEL);
		insert.setIn2(false);
		insert.setIn3(TOKEN);
		proxy.insertRecord(insert);

		LOG.info("Update record");
		record = new ImportRecord();
		record.setId("123");
		record.setRecord(Arrays.asList(newEntry("Name", "New title of the product")));
		UpdateRecord update = new UpdateRecord();
		update.setIn0(record);
		update.setIn1(CHANNEL);
		update.setIn2(false);
		update.setIn3(TOKEN);
		proxy.updateRecord(update);

		LOG.info("Delete record");
		DeleteRecord delete = new DeleteRecord();
		delete.setIn0("123");
		delete.setIn1(CHANNEL);
		delete.setIn2(false);
		delete.setIn3(TOKEN);
		proxy.deleteRecord(delete);

		LOG.info("Start product data import");
		StartImport startImport = new StartImport();
		startImport.setIn0(CHANNEL);
		startImport.setIn1(TOKEN);
		proxy.startImport(startImport);

		LOG.info("Start suggest data import");
		StartSuggestImport startSuggestImport = new StartSuggestImport();
		startSuggestImport.setIn0(CHANNEL);
		startSuggestImport.setIn1(TOKEN);
		proxy.startSuggestImport(startSuggestImport);

		LOG.info("All finished successfully");
	}

	private static List<String2StringMapEntry> getRecordForInsertion() {
		final List<String2StringMapEntry> map = new ArrayList<>();
		map.add(newEntry("Name", "Name Of the Product"));
		map.add(newEntry("Brand", "Brand name of the product"));
		map.add(newEntry("EAN", "1234567891320"));
		map.add(newEntry("ArticleNumber", "24960999"));
		map.add(newEntry("Description", "Produktbeschreibung"));
		map.add(newEntry("Price", "10"));
		return map;
	}

}
