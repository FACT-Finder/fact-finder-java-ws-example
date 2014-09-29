package de.factfinder.runner;

import java.rmi.RemoteException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws69.UserManagementPortTypeProxy;
import de.factfinder.wsclient.ws69.AuthenticationToken;
import de.factfinder.wsclient.ws69.usermanagement.Group;
import de.factfinder.wsclient.ws69.usermanagement.Role;
import de.factfinder.wsclient.ws69.usermanagement.User;

/**
 * This class demonstrates the use of the user management.
 */
public final class RunnerUserManagement {
	private static final Logger		LOG			= Logger.getLogger(RunnerUserManagement.class.getCanonicalName());
	private static final String		GROUP_NAME	= "testgroup";
	private static final Role[]		ROLES		= {Role.InstallManager, Role.MessagesManager};
	private static final String		USER_NAME	= "testuser";
	private static final String		PASSWORD	= "testpw";
	private static final String[]	GROUP_NAMES	= {"Administrator"};
	private static final String[]	CHANNELS	= {"de", "at"};

	private RunnerUserManagement() {
	}

	/**
	 * Runs the sample program.
	 * 
	 * @param args Unused.
	 */
	public static void main(final String[] args) {
		sendRequest(Settings.getUrl(WebServiceUrlType.USERMANAGEMENT));
	}

	/**
	 * Sends a search request to the endpoint and prints the result.
	 * 
	 * @param endpoint The web service URL.
	 */
	private static void sendRequest(final String endpoint) {
		// Timeout.setTimeoutValue(TIMEOUT);
		final UserManagementPortTypeProxy proxy = new UserManagementPortTypeProxy(endpoint);
		createGroups(proxy);
		deleteGroup(proxy);
		createUser(proxy);
		deleteUser(proxy);
	}

	/**
	 * Creates a group if it does not exists already.
	 * 
	 * @param proxy The user management webservice.
	 */
	private static void createGroups(final UserManagementPortTypeProxy proxy) {
		final AuthenticationToken token = Settings.getAuthToken();
		try {
			final Group group = proxy.getGroup(GROUP_NAME, token);
			if (group == null) {
				final Group newGroup = proxy.createGroup(GROUP_NAME, ROLES, token);
				LOG.info("Group [name=" + newGroup.getName() + ", roles=" + Arrays.toString(newGroup.getRoles()) + "] created.");
			} else {
				LOG.info("Group [name=" + group.getName() + ", roles=" + Arrays.toString(group.getRoles()) + "]" + " already exists.");
			}

		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}

	/**
	 * Deletes a group if possible.
	 * 
	 * @param proxy The user management webservice.
	 */
	private static void deleteGroup(final UserManagementPortTypeProxy proxy) {
		final AuthenticationToken token = Settings.getAuthToken();
		try {
			final Group group = proxy.getGroup(GROUP_NAME, token);
			if (group == null) {
				LOG.info("Group [name=" + GROUP_NAME + "] not deleted because it does not exist");
			} else {
				proxy.deleteGroup(GROUP_NAME, token);
				LOG.info("Group [name=" + group.getName() + ", roles=" + Arrays.toString(group.getRoles()) + ']' + " deleted.");
			}

		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}

	/**
	 * 
	 * Creates a user if it does not exists already.
	 * 
	 * @param proxy The user management webservice.
	 */
	private static void createUser(final UserManagementPortTypeProxy proxy) {
		final AuthenticationToken token = Settings.getAuthToken();
		final User user;
		try {
			user = proxy.getUser(USER_NAME, token);
			if (user == null) {
				final User newUser = proxy.createUser(USER_NAME, PASSWORD, GROUP_NAMES, CHANNELS, ROLES, token);
				LOG.info("User [name=" + newUser.getName() + ", groups=" + Arrays.toString(populateGroupsNames(newUser.getGroups())) + ", channels="
						+ Arrays.toString(newUser.getChannels()) + ", roles=" + Arrays.toString(newUser.getRoles()) + "] created.");
			} else {
				LOG.info("User [name=" + user.getName() + ", groups=" + Arrays.toString(populateGroupsNames(user.getGroups())) + ", channels="
						+ Arrays.toString(user.getChannels()) + ", roles=" + Arrays.toString(user.getRoles()) + "] already exists.");
			}
		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}

	/**
	 * Deletes a user if possible.
	 * 
	 * @param proxy The user management webservice.
	 */
	private static void deleteUser(final UserManagementPortTypeProxy proxy) {
		final AuthenticationToken token = Settings.getAuthToken();
		try {
			final User user = proxy.getUser(USER_NAME, token);
			if (user == null) {
				LOG.info("User [name=" + USER_NAME + "] not deleted because it does not exist");
			} else {
				proxy.deleteUser(USER_NAME, token);
				LOG.info("User [name=" + user.getName() + ", groups=" + Arrays.toString(populateGroupsNames(user.getGroups())) + ", channels="
						+ Arrays.toString(user.getChannels()) + ", roles=" + Arrays.toString(user.getRoles()) + "] deleted.");
			}

		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}

	/**
	 * A helper method which populates the group names.
	 * 
	 * @param groups the groups.
	 * @return a String[] containing the group names.
	 */
	private static String[] populateGroupsNames(final Group[] groups) {
		final String[] groupNames = new String[groups.length];
		for (int i = 0; i < groups.length; i++) {
			groupNames[i] = groups[i].getName();
		}
		return groupNames;
	}

}
