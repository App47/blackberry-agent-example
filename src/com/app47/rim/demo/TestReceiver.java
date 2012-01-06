package com.app47.rim.demo;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

public abstract class TestReceiver extends MainScreen {

	public TestReceiver() {
		super();
	}

	// implementam onPause + onResume (onUIEngineAttached)
	protected void onUiEngineAttached(boolean attached) {
		if (attached == true) {
			// onResume
			appResumed();
		} else {
			appPaused();
		}
	}

	// UI only
	public abstract void appResumed();

	// UI only
	public abstract void appPaused();

	protected void makeMenu(Menu menu, int instance) {

		menu.add(new MenuItem("Logging", 1, 100) {
			public void run() {
				UiApplication.getUiApplication().pushScreen(new Logging());
			}
		});
		menu.add(new MenuItem("Timed Events", 2, 101) {
			public void run() {
				UiApplication.getUiApplication().pushScreen(new TimedEvents());
			}
		});
		menu.add(new MenuItem("View cached sessions", 3, 102) {
			public void run() {
				UiApplication.getUiApplication().pushScreen(
						new CachedSessions());
			}
		});
		menu.add(new MenuItem("View config info", 4, 103) {
			public void run() {
				UiApplication.getUiApplication().pushScreen(new App47Screen());
			}
		});
		// super.makeMenu(menu, instance);
	}
}
