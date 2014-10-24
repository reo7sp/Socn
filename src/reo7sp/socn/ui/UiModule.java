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

import java.awt.TrayIcon;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import reo7sp.socn.Module;
import reo7sp.socn.social.SocialModule;
import reo7sp.socn.social.SocialNetwork;

/**
 * Created by reo7sp on 10/19/13 at 4:31 PM
 */
public class UiModule extends Module {
	private static final UiModule INSTANCE = new UiModule();
	private final ImageFactory imageFactory = new ImageFactory();
	private final Map<SocialNetwork, HashSet<String>> notifications = new HashMap<SocialNetwork, HashSet<String>>();
	private final Map<SocialNetwork, HashSet<String>> immutableNotifications = Collections.unmodifiableMap(notifications);
	private final MainWindow mainWindow = new MainWindow();
	private final TwitterLoginWindow twitterLoginWindow = new TwitterLoginWindow();
	private final VkLoginWindow vkLoginWindow = new VkLoginWindow();
	private final DateFormat dateFormat = new SimpleDateFormat("dd MMM HH:mm:ss");
	private final PopupManager popupManager = new PopupManager();
	private TrayManager trayManager;
	private Window lastVisibleWindow;

	private UiModule() {
	}

	public static UiModule getInstance() {
		return INSTANCE;
	}

	@Override
	public void enable() throws Exception {
		trayManager = new TrayManager();
		lastVisibleWindow = mainWindow;

	}

	@Override
	public void disable() throws Exception {
		mainWindow.setVisible(false);
		trayManager.removeIcon();
	}

	public void setVisibleWindow(Class<? extends Window> c) {
		if (c == null) {
			mainWindow.setVisible(false);
			twitterLoginWindow.setVisible(false);
			vkLoginWindow.setVisible(false);
		} else if (c == mainWindow.getClass()) {
			mainWindow.setVisible(true);

			twitterLoginWindow.setVisible(false);
			vkLoginWindow.setVisible(false);

			lastVisibleWindow = mainWindow;
		} else if (c == twitterLoginWindow.getClass()) {
			twitterLoginWindow.setVisible(true);

			mainWindow.setVisible(false);
			vkLoginWindow.setVisible(false);

			lastVisibleWindow = twitterLoginWindow;
		} else if (c == vkLoginWindow.getClass()) {
			vkLoginWindow.setVisible(true);

			mainWindow.setVisible(false);
			twitterLoginWindow.setVisible(false);

			lastVisibleWindow = vkLoginWindow;
		}
	}

	public void notify(SocialNetwork socialNetwork, String message) {
		synchronized (notifications) {
			HashSet<String> messages = notifications.get(socialNetwork);
			if (messages == null) {
				messages = new HashSet<String>();
				messages.add(message);
				notifications.put(socialNetwork, messages);
			} else {
				messages.add(message);
			}

			trayManager.updateIcon();

			popupManager.sendMessage(socialNetwork.getName(), message, TrayIcon.MessageType.INFO);

			StringBuilder content = new StringBuilder();
			for (String s : messages) {
				content.append(s).append("\n");
			}
			mainWindow.updateMessages(socialNetwork, content.toString());
		}
	}

	public void clearNotifications(SocialNetwork socialNetwork) {
		synchronized (notifications) {
			notifications.remove(socialNetwork);
			trayManager.updateIcon();
			mainWindow.updateMessages(socialNetwork, "");
		}
	}

	void clearNotifications() {
		synchronized (notifications) {
			notifications.clear();
			trayManager.updateIcon();
			for (SocialNetwork socialNetwork : SocialModule.getInstance().getSocialNetworks()) {
				mainWindow.updateMessages(socialNetwork, "");
			}
		}
	}

	public boolean isWindowVisible() {
		return lastVisibleWindow != null && lastVisibleWindow.isVisible();
	}

	public void setWindowVisible(boolean b) {
		if (lastVisibleWindow != null) {
			lastVisibleWindow.setVisible(b);
		}
	}

	public void log(String s) {
		if (mainWindow != null) {
			mainWindow.log(s);
		}
	}

	public void showPopup(String title, String content, TrayIcon.MessageType type) {
		popupManager.sendMessage(title, content, type);
	}

	void displayMessage(String caption, String text, TrayIcon.MessageType type) {
		trayManager.displayMessage(caption, text, type);
	}

	ImageFactory getImageFactory() {
		return imageFactory;
	}

	Map<SocialNetwork, HashSet<String>> getNotifications() {
		return immutableNotifications;
	}
}
