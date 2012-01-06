package com.app47.rim.demo;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app47.embeddedagent.IOHelperAgent;
import com.app47.embeddedagent.ServiceEventAgent;

public class TimedEvents extends TestReceiver {
	private static String selectedUUID;

	private static VerticalFieldManager vert;
	private static BasicEditField genericEdit;
	private static ButtonField genericButton;
	private static ButtonField timedButton;
	private static BasicEditField timedEdit;
	private static ObjectChoiceField spinner;
	private static ButtonField finishEvent;

	private static Vector tags = new Vector();

	private VerticalFieldManager eventsManager;

	public TimedEvents() {
		vert = new VerticalFieldManager();
		vert.add(new LabelField("Start timed event"));

		HorizontalFieldManager genericHor = new HorizontalFieldManager();
		genericHor.add(genericEdit = new BasicEditField("Name : ", ""));
		vert.add(genericHor);

		HorizontalFieldManager timedHor = new HorizontalFieldManager();
		timedHor.add(timedEdit = new BasicEditField("Tags : ", ""));
		vert.add(timedHor);
		vert.add(genericButton = new ButtonField("Start generic"));
		vert.add(timedButton = new ButtonField("Start timed"));

		vert.add(new SeparatorField());

		vert.add(new LabelField("Finish a timed event"));
		vert.add(spinner = new ObjectChoiceField());
		vert.add(finishEvent = new ButtonField("Finish event"));
		vert.add(new SeparatorField());
		vert.add(new LabelField("Active events"));
		vert.add(new SeparatorField());
		vert.add(eventsManager = new VerticalFieldManager());
		add(vert);

		genericButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				// get name for the generic event
				String timedEventTag = timedEdit.getText();
				String tag = genericEdit.getText();
				startTime(timedEventTag, tag);
				genericEdit.setText("");
				timedEdit.setText("");
				setupView();
			}
		});

		timedButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				// get tag for timed event
				String timedEventTag = timedEdit.getText();
				String tag = genericEdit.getText();
				startTimedEvent(timedEventTag, tag);
				genericEdit.setText("");
				timedEdit.setText("");
				setupView();
			}
		});

		finishEvent.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				selectedUUID = (String) spinner.getChoice(spinner
						.getSelectedIndex());
				endTimedEvent();
				setupView();
			}
		});
		// onPause !! - writeActiveTimedEvents()
		// de fiecare data cand se apeleaza onResume in EmbeddedAgentUI - se
		// apeleaza onResumeSessionCheck() -dupa care -
		// AgentEventHelper.onResumeEventCheck();

	}

	// on click - Start Timed
	public static void startTimedEvent(String timedEvent, String tagsEvent) {
		// get text from name and tag edit test

		tags.addElement(timedEvent);
		// tags.addElement("going");

		ServiceEventAgent.startTimedEvent(tagsEvent, tags);
		JSONObject activeEvents = IOHelperAgent.loadActiveTimedEvents();
		JSONArray names = activeEvents.names();
		for (int x = 0; x < 1; x++) {
			try {
				selectedUUID = names.get(x).toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// selectedUUID = names.toString();
	}

	// Send Generic
	public static void startTime(String timeEventTag, String tagsEvent) {
		tags.addElement(tagsEvent);
		ServiceEventAgent.handleRequest(ServiceEventAgent.SEND_GENERIC_EVENT,
				tags, timeEventTag, null);
	}

	// Finish Event
	public static void endTimedEvent() {
		if (selectedUUID == null) {
			Dialog.alert("No event selected");
			// Toast.makeText(this,"No UUID selected",
			// Toast.LENGTH_SHORT).show();
		} else {

			ServiceEventAgent.handleRequest(ServiceEventAgent.FINISH_TIMED,
					null, null, selectedUUID);
		}
	}

	// se va apela dupa fiecare apasare de buton si cand incepe activitatea
	public void setupView() {
		// add to left screen
		// TODO add to events list
		JSONObject activeEvents = IOHelperAgent.loadActiveTimedEvents();
		eventsManager.deleteAll();
		addTimedEvents(activeEvents);
		// TODO add to spinner/object choice field
		JSONArray names = activeEvents.names();
		// returneaza n item-uri - names - uuid-urile
		if (names != null) {
			String[] uuids = new String[names.length()];
			for (int i = 0; i < names.length(); i++) {
				try {
					uuids[i] = names.getString(i);
				} catch (JSONException e) {
					uuids = null;
				}
			}

			if (uuids != null) {
				spinner.setChoices(uuids);
			} else {
				String str[] = { "" };
				spinner.setChoices(str);
			}
		}
		// to do - place uuid-urile on screen

		JSONObject finishEvents = IOHelperAgent.loadFinishedEvents();
		// add finishEvents to finish Events screen

		/**
		 * JSONArray names = activeEvents.names(); if(names != null){ String[]
		 * uuids = new String[names.length()]; for(int i=0; i<names.length();
		 * i++){ try{ uuids[i] = names.getString(i); }catch(JSONException e){
		 * e.printStackTrace(); } } ArrayAdapter<String> spinnerArrayAdapter =
		 * new ArrayAdapter<String>(this, R.layout.spinnerlayout, uuids);
		 * spinner.setAdapter(spinnerArrayAdapter);
		 */
	}

	private void addTimedEvents(JSONObject activeEvents) {
		if (activeEvents != null) {
			Enumeration keys = activeEvents.keys();
			while (keys.hasMoreElements()) {
				try {
					JSONObject objToDisplay = (JSONObject) activeEvents
							.get((String) keys.nextElement());
					String startTime = objToDisplay.getString("start_time");
					String name = objToDisplay.getString("name");
					String uuid = objToDisplay.getString("uuid");
					int duration = objToDisplay.getInt("duration");
					long startTimeRaw = objToDisplay.getLong("start_time_raw");

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss:SSSS z");

					eventsManager.add(new SeparatorField());
					eventsManager.add(new LabelField("Name : " + name));
					eventsManager.add(new LabelField("UUID : " + uuid));
					eventsManager.add(new LabelField("Duration : " + duration));
					eventsManager.add(new LabelField("StartTimeRaw : "
							+ startTimeRaw));
					eventsManager.add(new LabelField("Start Time : "
							+ sdf.format(new Date(Long.parseLong(startTime)))));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void appResumed() {
		setupView();
	}

	public void appPaused() {
		// TODO Auto-generated method stub

	}
}
