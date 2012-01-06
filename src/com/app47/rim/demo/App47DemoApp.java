package com.app47.rim.demo;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;

import com.app47.embeddedagent.EmbeddedAgent;
import com.app47.embeddedagent.EmbeddedAgentLogger;
import com.app47.embeddedagent.HelperSessionAgent;
import com.app47.embeddedagent.IOHelperLogAgent;

/**
 * This class extends the UiApplication class, providing a graphical user
 * interface.
 */
public class App47DemoApp extends UiApplication {

	public static void main(String args[]) {
		App47DemoApp agent = new App47DemoApp();
		agent.enterEventDispatcher();
	}

	public void deactivate() {
		EmbeddedAgent.onPause();
		super.deactivate();
	}

	public void activate() {
		EmbeddedAgent.onResume();
		super.activate();
	}

	public App47DemoApp() {
		try {
			ApplicationPermissionsManager permManager = ApplicationPermissionsManager
					.getInstance();

			if (permManager
					.getPermission(ApplicationPermissions.PERMISSION_FILE_API) != ApplicationPermissions.VALUE_ALLOW) {
				// Request our permission to inject events
				ApplicationPermissions pAppPermission = new ApplicationPermissions();
				pAppPermission
						.addPermission(ApplicationPermissions.PERMISSION_FILE_API);
			}

			pushScreen(new App47Screen());
		} catch (Throwable e) {
			e.getMessage();
			if (!IOHelperLogAgent.hasCaughtUnhandled()) {
				EmbeddedAgentLogger.wtf(
						"Uncaught Exception! - " + e.getMessage(), null);
				IOHelperLogAgent.setHasCaughtUnhandled(true);
				HelperSessionAgent.onPauseSessionCheck(); // on pause won't be
															// called
			}

			// after saving into the log, exit app
			System.exit(1);
		}
	}
	
}
