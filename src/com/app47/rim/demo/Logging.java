package com.app47.rim.demo;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app47.embeddedagent.EmbeddedAgentLogger;
import com.app47.embeddedagent.IOHelperConfigAgent;
import com.app47.embeddedagent.IOHelperLogAgent;
import com.app47.embeddedagent.JSONKeys;

public class Logging extends TestReceiver {
	private final static int DEBUG = 901;
	private final static int INFO = 902;
	private final static int WARN = 903;
	private final static int ERROR = 904;
	private final static int CRASH1 = 905;
	private final static int CRASH2 = 906;
	private final static int CRASH3 = 907;
	private int button;
	private static VerticalFieldManager manager;
	public static Logging instance;

	public Logging() {
		instance = this;
	}

	// Override
	public void appResumed() {
		ButtonField debug = new ButtonField("Debug");
		debug.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				button = DEBUG;
				generateLog();
			}
		});

		ButtonField info = new ButtonField("Info");
		info.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				button = INFO;
				generateLog();
			}
		});

		ButtonField warn = new ButtonField("Warn");
		warn.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				button = WARN;
				generateLog();
			}
		});

		ButtonField error = new ButtonField("error");
		error.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				button = ERROR;
				generateLog();
			}
		});

		ButtonField crash1 = new ButtonField("Crash 1");
		crash1.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				button = CRASH1;
				generateLog();
			}
		});

		ButtonField crash2 = new ButtonField("Crash 2");
		crash2.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				button = CRASH2;
				generateLog();
			}
		});

		ButtonField crash3 = new ButtonField("Crash 3");
		crash3.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				button = CRASH3;
				generateLog();
			}
		});

		HorizontalFieldManager hor1 = new HorizontalFieldManager();
		hor1.add(debug);
		hor1.add(info);
		add(hor1);

		HorizontalFieldManager hor2 = new HorizontalFieldManager();
		hor2.add(warn);
		hor2.add(error);
		add(hor2);

		HorizontalFieldManager hor3 = new HorizontalFieldManager();
		hor3.add(crash1);
		hor3.add(crash2);
		hor3.add(crash3);
		add(hor3);

		manager = new VerticalFieldManager();
		add(manager);

		setupView();
	}

	// Override
	public void appPaused() {
	}

	public void setupView() {
		String level = null;
		try {
			level = IOHelperConfigAgent.loadConfig().getString(
					JSONKeys.REG_RESPONSE_AGENT_LOG_LEVEL);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (level == null) {
			level = "none";
		}

		LabelField loggingLevel = new LabelField("Log level : " + level);
		manager.add(loggingLevel);
		manager.add(new SeparatorField());

		try {

			JSONArray logs = IOHelperLogAgent.loadLogs();
			if (logs != null) {
				int size = logs.length();
				for (int i = 0; i < size; i++) {
					JSONObject obj;
					obj = logs.getJSONObject(i);

					Enumeration enum = obj.keys();
					while (enum.hasMoreElements()) {
						String elem = enum.nextElement().toString();
						LabelField label = new LabelField(elem + " : "
								+ obj.get(elem));
						manager.add(label);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void generateLog() {
		String message = "Message";
		Vector tags = new Vector();
		tags.addElement("crash");

		String level = "None";

		switch (button) {
		case DEBUG:
			level = "Debug";
			EmbeddedAgentLogger.d(message, tags);
			break;
		case INFO:
			level = "Info";
			EmbeddedAgentLogger.i(message, tags);
			break;
		case WARN:
			level = "Warn";
			EmbeddedAgentLogger.w(message, tags);
			break;
		case ERROR:
			level = "Error";
			EmbeddedAgentLogger.e(message, tags);
			break;
		case CRASH1:
			level = "Crash";
			EmbeddedAgentLogger.wtf(message, tags);
			break;
		case CRASH2:
			throw new NullPointerException();
		case CRASH3:
			throw new ClassCastException();
		}

		String agentLevel = null;

		try {
			agentLevel = IOHelperConfigAgent.loadConfig().getString(
					JSONKeys.REG_RESPONSE_AGENT_LOG_LEVEL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (agentLevel == null)
			agentLevel = "None";

		if (agentLevel == null || levelToInt(level) < levelToInt(agentLevel)) {
			Dialog.alert("Log level too high");
		}
	}

	private static int levelToInt(String theLevel) {
		String level = theLevel.toLowerCase();
		if (level.equals("debug")) {
			return 1;
		} else if (level.equals("info")) {
			return 2;
		} else if (level.equals("warn")) {
			return 3;
		} else if (level.equals("error")) {
			return 4;
		} else if (level.equals("crash")) {
			return 5;
		} else
			return 100;
	}
}
