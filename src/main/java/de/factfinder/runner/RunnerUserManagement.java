package de.factfinder.runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws71.CreateGroup;
import de.factfinder.adapters.wsclient.ws71.CreateUser;
import de.factfinder.adapters.wsclient.ws71.DeleteGroup;
import de.factfinder.adapters.wsclient.ws71.GetGroup;
import de.factfinder.adapters.wsclient.ws71.GetUser;
import de.factfinder.adapters.wsclient.ws71.GroupAlreadyExistsException;
import de.factfinder.adapters.wsclient.ws71.GroupNotFoundException;
import de.factfinder.adapters.wsclient.ws71.InvalidGroupException;
import de.factfinder.adapters.wsclient.ws71.UserManagementPortType;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws71.usermanagement.Group;
import de.factfinder.wsclient.ws71.usermanagement.Role;
import de.factfinder.wsclient.ws71.usermanagement.User;

/**
 * This class demonstrates the use of the user management.
 */
public final class RunnerUserManagement {
	private static final Logger			LOG			= Logger.getLogger(RunnerUserManagement.class.getCanonicalName());
	private static final String			GROUP_NAME	= "testgroup111";
	private static final List<Role>		ROLES		= Arrays.asList(Role.INSTALL_MANAGER, Role.MESSAGES_MANAGER);
	private static final String			USER_NAME	= "testuser1";
	private static final String			PASSWORD	= "testpw1";
	private static final List<String>	GROUP_NAMES	= Arrays.asList("Administrator");
	private static final List<String>	CHANNELS	= Arrays.asList("bergfreunde-de", "bikester-at");

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
		final UserManagementPortType proxy = Service.get(UserManagementPortType.class, endpoint);
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
	private static void createGroups(final UserManagementPortType proxy) {
		final CreateGroup create = new CreateGroup();
		create.setIn0(GROUP_NAME);
		create.setIn1(ROLES);
		create.setIn2(Settings.getAuthToken());

		try {
			final Group newGroup = proxy.createGroup(create).getOut();
			LOG.info("Group [name=" + newGroup.getName() + ", roles=" + newGroup.getRoles() + "] created.");
		} catch (final GroupAlreadyExistsException e) {
			LOG.info("Group [name=" + GROUP_NAME + ", roles=" + ROLES + "] already exists.");
		} catch (final InvalidGroupException e) {
			LOG.info("Group [name=" + GROUP_NAME + ", roles=" + ROLES + "] is invalid.");
		}
	}

	/**
	 * Deletes a group if possible.
	 * 
	 * @param proxy The user management webservice.
	 */
	private static void deleteGroup(final UserManagementPortType proxy) {
		final GetGroup get = new GetGroup();
		get.setIn0(GROUP_NAME);
		get.setIn1(Settings.getAuthToken());

		final Group group = proxy.getGroup(get).getOut();
		if (group == null) {
			LOG.info("Group [name=" + GROUP_NAME + "] not deleted because it does not exist");
		} else {
			final DeleteGroup delete = new DeleteGroup();
			delete.setIn0(GROUP_NAME);
			delete.setIn1(Settings.getAuthToken());
			proxy.deleteGroup(delete);
			LOG.info("Group [name=" + group.getName() + ", roles=" + group.getRoles() + "] deleted.");
		}
	}

	/**
	 * 
	 * Creates a user if it does not exists already.
	 * 
	 * @param proxy The user management webservice.
	 */
	private static void createUser(final UserManagementPortType proxy) {
		final GetUser get = new GetUser();
		get.setIn0(USER_NAME);
		get.setIn1(Settings.getAuthToken());
		final User user = proxy.getUser(get).getOut();

		if (user == null) {
			final CreateUser create = new CreateUser();
			create.setIn0(USER_NAME);
			create.setIn1(PASSWORD);
			create.setIn2(GROUP_NAMES);
			create.setIn3(CHANNELS);
			create.setIn4(ROLES);
			create.setIn5(Settings.getAuthToken());
			try {
				User newUser = proxy.createUser(create).getOut();
				LOG.info("User [name=" + newUser.getName() + ", groups=" + joinGroupNames(newUser.getGroups()) + ", channels=" + newUser.getChannels()
						+ ", roles=" + newUser.getRoles() + "] created.");
			} catch (final GroupNotFoundException e) {
				LOG.error(null, e);
			}
		} else {
			LOG.info("User [name=" + user.getName() + ", groups=" + joinGroupNames(user.getGroups()) + ", channels=" + user.getChannels() + ", roles="
					+ user.getRoles() + "] already exists.");
		}
	}

	/**
	 * Deletes a user if possible.
	 * 
	 * @param proxy The user management webservice.
	 */
	private static void deleteUser(final UserManagementPortType proxy) {
		final GetUser get = new GetUser();
		get.setIn0(USER_NAME);
		get.setIn1(Settings.getAuthToken());
		final User user = proxy.getUser(get).getOut();
		if (user == null) {
			LOG.info("User [name=" + USER_NAME + "] not deleted because it does not exist");
		} else {
			final DeleteGroup delete = new DeleteGroup();
			delete.setIn0(GROUP_NAME);
			delete.setIn1(Settings.getAuthToken());
			LOG.info("User [name=" + user.getName() + ", groups=" + joinGroupNames(user.getGroups()) + ", channels=" + user.getChannels() + ", roles="
					+ user.getRoles() + "] deleted.");
		}
	}

	/**
	 * A helper method which populates the group names.
	 * 
	 * @param groups the groups.
	 * @return a String containing the group names separated with {@code ,}.
	 */
	public static String joinGroupNames(final List<Group> groups) {
		final List<String> names = new ArrayList<>();
		for (final Group group : groups) {
			names.add(group.getName());
		}
		return StringUtils.join(names, ", ");
	}
}
