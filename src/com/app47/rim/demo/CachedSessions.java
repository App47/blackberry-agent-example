package com.app47.rim.demo;

import java.util.Date;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.app47.embeddedagent.HelperEnvironmentAgent;
import com.app47.embeddedagent.IOHelperSessionAgent;
import com.app47.embeddedagent.ObjectAgentSession;
import com.app47.embeddedagent.ServiceEventAgent;

public class CachedSessions extends TestReceiver {
	public static CachedSessions instance;
	private static VerticalFieldManager manager;

	public CachedSessions() {
		instance = this;
	}

	public void setupView() {
		Vector ses = IOHelperSessionAgent.loadSessions();
		if (ses != null) {
			int size = ses.size();
			for (int i = 0; i < size; i++) {
				ObjectAgentSession session = (ObjectAgentSession) ses
						.elementAt(i);

				String[] fields = { "UUID:", "Start Time: ", "End Time: ",
						"Duration: " };

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss:SSSS z");

				long startLong = Long.parseLong(HelperEnvironmentAgent
						.longToDate(session.getStartTime()));
				long endLong = Long.parseLong(HelperEnvironmentAgent
						.longToDate(session.getStartTime()
								+ session.getDuration() * 1000));

				String startTime = sdf.format(new Date(startLong));
				String endTime = sdf.format(new Date(endLong));

				String[] params = { session.getUuid(), startTime, endTime,
						String.valueOf(session.getDuration()) };
				for (int j = 0; j < fields.length; j++) {
					LabelField field = new LabelField(fields[j] + " : "
							+ params[j]);
					manager.add(field);
				}
				manager.add(new SeparatorField());

				// TODO clau apelare
			}
		}
		ServiceEventAgent.handleRequest(ServiceEventAgent.RESUME, null, null,
				null);
	}

	public void appResumed() {
		manager = new VerticalFieldManager();
		add(manager);
		setupView();
	}

	public void appPaused() {
	}
}