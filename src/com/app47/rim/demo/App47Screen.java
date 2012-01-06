package com.app47.rim.demo;

import java.util.Enumeration;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app47.embeddedagent.EmbeddedAgent;
import com.app47.embeddedagent.IOHelperAgent;
import com.app47.embeddedagent.IOHelperConfigAgent;
import com.app47.embeddedagent.JSONKeys;
/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public class App47Screen extends TestReceiver {
	private static LabelField logLevel;
	private static LabelField agentEnabled;
	private static LabelField agentId;
	private static LabelField configurationEndpoint;
	private static LabelField deviceIdentifier;
	private static LabelField updateDelay;
	private static LabelField updateFrequency;
	private static LabelField appId;
	private static LabelField noOfGroups;
	private static LabelField deviceEnabled;
	private static LabelField realtimeDataEndpoint;
	private static LabelField serverTimeEpoch;
	private static LabelField sessionDataEndpoint;
	private static LabelField serverTimeOffset;
	private static VerticalFieldManager vert;

	/**
	 * on pause
	 */
	public App47Screen() {

		logLevel = new LabelField("Log level : ");
		agentId = new LabelField("Agent id : ");
		agentEnabled = new LabelField("Agent enabled : ");
		configurationEndpoint = new LabelField("Configuration endpoint : ");
		deviceIdentifier = new LabelField("Device identifier : ");
		updateDelay = new LabelField("Update delay : ");
		updateFrequency = new LabelField("Update frequency : ");
		appId = new LabelField("Application ID : ");
		noOfGroups = new LabelField("Configuration groups : ");
		deviceEnabled = new LabelField("Device enabled : ");
		realtimeDataEndpoint = new LabelField("Realtime data endpoint : ");
		serverTimeEpoch = new LabelField("Server time epoch : ");
		sessionDataEndpoint = new LabelField("Session data endpoint : ");
		serverTimeOffset = new LabelField("Server time offset : ");

		add(logLevel);
		add(new SeparatorField());
		add(agentId);
		add(new SeparatorField());
		add(agentEnabled);
		add(new SeparatorField());
		add(configurationEndpoint);
		add(new SeparatorField());
		add(deviceIdentifier);
		add(new SeparatorField());
		add(updateDelay);
		add(new SeparatorField());
		add(updateFrequency);
		add(new SeparatorField());
		add(appId);
		add(new SeparatorField());
		add(noOfGroups);
		add(new SeparatorField());
		add(deviceEnabled);
		add(new SeparatorField());
		add(realtimeDataEndpoint);
		add(new SeparatorField());
		add(serverTimeEpoch);
		add(new SeparatorField());
		add(sessionDataEndpoint);
		add(new SeparatorField());
		add(serverTimeOffset);
		add(new SeparatorField());

		vert = new VerticalFieldManager();
		add(vert);

		EmbeddedAgent.configureAgent();
		updateConfig();
	}

	// Override
	public void appResumed() {
		updateConfig();
	}

	// Override
	public void appPaused() {
	}

	public boolean onClose() {
		EmbeddedAgent.onPause();
		return super.onClose();
	}

	public void setupView() {
		System.out.println("AGENT " + "Coming from ConfigScreen");
	}

	public static void updateConfig() {
		try {
			JSONObject config = IOHelperConfigAgent.loadConfig();
			logLevel.setText("Log level : "
					+ config.get(JSONKeys.REG_RESPONSE_AGENT_LOG_LEVEL));
			agentId.setText("Agent id : "
					+ config.get(JSONKeys.REG_RESPONSE_AGENT_ID));
			agentEnabled.setText("Agent enabled : "
					+ config.get(JSONKeys.REG_RESPONSE_AGENT_ENABLED));
			configurationEndpoint.setText("Configuration endpoint : "
					+ config.get(JSONKeys.CONFIGURATION_ENDPOINT));
			deviceIdentifier.setText("Device identifier : "
					+ config.get(JSONKeys.SHOW_IDENTIFIER));
			updateDelay.setText("Update delay : "
					+ config.get(JSONKeys.UPLOAD_DELAY));
			updateFrequency.setText("Update frequency : "
					+ config.get(JSONKeys.UPDATE_FRQ));
			appId.setText("Application ID : " + config.get(JSONKeys.APP_ID));
			noOfGroups.setText("Configuration groups : "
					+ ((JSONArray) config
							.get(JSONKeys.REG_RESPONSE_CONFIGURATION_GROUPS))
							.length());
			deviceEnabled.setText("Device enabled : "
					+ config.get(JSONKeys.REG_RESPONSE_DEVICE_ENABLED));
			realtimeDataEndpoint.setText("Realtime data endpoint : "
					+ config.get(JSONKeys.REG_RESPONSE_REALTIME_DATA_ENDPOINT));
			serverTimeEpoch.setText("Server time epoch : "
					+ config.get(JSONKeys.REG_RESPONSE_SERVER_TIME_EPOCH));
			sessionDataEndpoint.setText("Session data endpoint : "
					+ config.get(JSONKeys.REG_RESPONSE_SESSION_DATA_ENDPOINT));
			serverTimeOffset.setText("Server time offset : "
					+ config.get("server_time_off"));

			updateConfigGroups();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void updateConfigGroups() {
		vert.deleteAll();
		String groupsToLoad = IOHelperAgent.readFile(IOHelperAgent.FILE_PATH
				+ IOHelperAgent.FILE_SEPARATOR
				+ IOHelperAgent.GROUPS_NAMES_FILE_NAME);
		JSONArray array = null;
		try {
			//groups to load is this: {"bb_test":"file:///store/home/embeddedAgent/groups_bb_test"}
			if (groupsToLoad != null) {
				array = new JSONArray("[" + groupsToLoad + "]");
			}

			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject fobj = array.getJSONObject(i);
					
					String fileName = null;
					for(Enumeration iter = fobj.keys(); iter.hasMoreElements();){
						String key = (String)iter.nextElement();
						fileName = fobj.getString(key);
					}					
					//filename is {"bb_test":"file:///store/home/embeddedAgent/groups_bb_test"}
					String group = IOHelperAgent.readFile(fileName); //this isn't working?

					if (group != null) {
						JSONObject groupObject = new JSONObject(group);
						VerticalFieldManager verti = new VerticalFieldManager();
						LabelField groupF = new LabelField("Group");
						LabelField name = new LabelField("Name : "
								+ groupObject.get(JSONKeys.CONF_GROUP_NAME));
						LabelField version = new LabelField("Version : "
								+ groupObject.getInt(JSONKeys.GROUPS_VERSION));

						verti.add(groupF);
						verti.add(name);
						verti.add(version);

						vert.add(verti);
						vert.add(new SeparatorField());
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
