package de.factfinder.runner.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.factfinder.adapters.wsclient.ws611.String2StringMapEntry;
import de.factfinder.wsclient.ws611.usermanagement.Group;

/**
 * A helper for creating Entries and printing ArrayOf* types.
 */
public class Helper {
	public static String	DELIMITER	= ",";



	public static String joinGroupNames(final List<Group> groups) {
		final List<String> names = new ArrayList<>();
		for (final Group group : groups) {
			names.add(group.getName());
		}
		return StringUtils.join(names, DELIMITER);
	}

	public static String2StringMapEntry newEntry(final String key, final String value) {
		final String2StringMapEntry entry = new String2StringMapEntry();
		entry.setKey(key);
		entry.setValue(value);
		return entry;
	}
}
