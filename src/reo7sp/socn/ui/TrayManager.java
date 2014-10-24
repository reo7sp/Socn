/*
 Copyright 2014 Reo_SP

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 	http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package reo7sp.socn.ui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import reo7sp.socn.Core;
import reo7sp.socn.Log;
import reo7sp.socn.Utils;
import reo7sp.socn.social.SocialModule;
import reo7sp.socn.social.SocialNetwork;

/**
 * Created by reo7sp on 10/19/13 at 4:34 PM
 */
public class TrayManager {
	private final TrayIcon trayIcon = new TrayIcon(UiModule.getInstance().getImageFactory().getImage("status_off"));
	private Status status = Status.OFF;

	TrayManager() throws AWTException {
		initIcon();
		startUpdater();
	}

	private void startUpdater() {
		new Thread("TrayIcon-Updater") {
			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(1500);
						updateIcon();
					}
				} catch (InterruptedException ignored) {
				}
			}
		}.start();
	}

	private void initIcon() throws AWTException {
		trayIcon.setImageAutoSize(true);
		trayIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				UiModule.getInstance().setWindowVisible(!UiModule.getInstance().isWindowVisible());
			}
		});
		trayIcon.setPopupMenu(generateMenu());
		updateIcon();
		SystemTray.getSystemTray().add(trayIcon);
	}

	void updateIcon() {
		if (UiModule.getInstance().getNotifications().isEmpty()) {
			updateStatus();
		} else {
			setStatus(status == Status.NEW ? Status.NEW_C : Status.NEW);
		}
		updateTooltip();
	}

	private void updateTooltip() {
		String s = "";
		for (SocialNetwork socialNetwork : SocialModule.getInstance().getSocialNetworks()) {
			if (socialNetwork.isConnected()) {
				HashSet<String> messages = UiModule.getInstance().getNotifications().get(socialNetwork);
				s += socialNetwork + ": " + (messages == null ? 0 : messages.size()) + "   ";
			} else {
				s += socialNetwork + ": -   ";
			}
		}
		trayIcon.setToolTip(s.trim());
	}

	private void updateStatus() {
		boolean noauth = true;
		boolean noconnect = false;
		for (SocialNetwork socialNetwork : SocialModule.getInstance().getSocialNetworks()) {
			if (socialNetwork.isLogined()) {
				noauth = false;
				if (!socialNetwork.isConnected()) {
					noconnect = true;
				}
				break;
			}
		}
		setStatus(noauth ? Status.OFF : noconnect ? Status.CONNECTING : Status.ON);
	}

	private PopupMenu generateMenu() {
		PopupMenu menu = new PopupMenu();

		MenuItem showItem = new MenuItem("Show window");
		showItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				UiModule.getInstance().setWindowVisible(!UiModule.getInstance().isWindowVisible());
			}
		});
		menu.add(showItem);

		MenuItem clearItem = new MenuItem("Clear notifications");
		clearItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				UiModule.getInstance().clearNotifications();
			}
		});
		menu.add(clearItem);

		menu.addSeparator();

		MenuItem twBrowserItem = new MenuItem("Open tw in browser");
		twBrowserItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					Utils.openBrowser("https://twitter.com/i/connect");
				} catch (Exception err) {
					Log.e("MainWindow", "Can't open tw page", err);
				}
			}
		});
		menu.add(twBrowserItem);

		MenuItem vkBrowserItem = new MenuItem("Open vk in browser");
		vkBrowserItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					Utils.openBrowser("http://vk.com/im");
				} catch (Exception err) {
					Log.e("MainWindow", "Can't open vk page", err);
				}
			}
		});
		menu.add(vkBrowserItem);

		menu.addSeparator();

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Core.shutdown();
			}
		});
		menu.add(exitItem);

		return menu;
	}

	private void setStatus(Status status) {
		trayIcon.setImage(UiModule.getInstance().getImageFactory().getImage("status_" + status.name().toLowerCase()));
		this.status = status;
	}

	void removeIcon() {
		SystemTray.getSystemTray().remove(trayIcon);
	}

	void displayMessage(String caption, String text, TrayIcon.MessageType type) {
		trayIcon.displayMessage(caption, text, type);
	}

	private static enum Status {
		ON,
		OFF,
		NEW,
		NEW_C,
		CONNECTING,
	}
}
