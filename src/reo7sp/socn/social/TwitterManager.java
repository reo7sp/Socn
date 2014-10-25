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

package reo7sp.socn.social;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import reo7sp.socn.Core;
import reo7sp.socn.Log;
import reo7sp.socn.Utils;
import reo7sp.socn.ui.MainWindow;
import reo7sp.socn.ui.TwitterLoginWindow;
import reo7sp.socn.ui.UiModule;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by reo7sp on 10/19/13 at 8:37 PM
 */
public class TwitterManager extends SocialNetwork {
	private static final TwitterManager INSTANCE = new TwitterManager();
	private final Twitter twitter = TwitterFactory.getSingleton();
	private final File authFile = new File(Core.getFolderLocation() + File.separator + "tw.auth");
	private final File settingsFile = new File(Core.getFolderLocation() + File.separator + "tw.settings");
	private final File secretsFile = new File(Core.getFolderLocation() + File.separator + "tw.secret");
	private RequestToken requestToken;
	private long lastMentionID;
	private boolean logined;
	private boolean connected = true;

	private TwitterManager() {
		try {
			String[] secrets = loadSecrets();
			twitter.setOAuthConsumer(secrets[0], secrets[1]);
		} catch (Exception err) {
			Log.w("TwitterManager", "Can't load secrets " + err);
		}
	}

	public static TwitterManager getInstance() {
		return INSTANCE;
	}

	@Override
	public void fastLogin() {
		try {
			endLogin(loadAuth());
			loadSettings();
		} catch (Exception err) {
		}
	}

	@Override
	public void disconnect() {
		twitter.setOAuthAccessToken(null);
		requestToken = null;
		authFile.delete();
		settingsFile.delete();
		logined = false;
		Log.i("VkManager", "Successfully disconnected from twitter!");
	}

	public void startLogin() throws Exception {
		twitter.setOAuthAccessToken(null);
		requestToken = twitter.getOAuthRequestToken();
		UiModule.getInstance().setVisibleWindow(TwitterLoginWindow.class);
		Utils.openBrowser(requestToken.getAuthorizationURL());
	}

	public void continueLogin(String pin) throws Exception {
		AccessToken accessToken;
		if (pin.isEmpty()) {
			accessToken = twitter.getOAuthAccessToken();
		} else {
			accessToken = twitter.getOAuthAccessToken(requestToken, pin);
		}

		saveAuth(accessToken);
		endLogin(accessToken);
		UiModule.getInstance().setVisibleWindow(MainWindow.class);
	}

	private void endLogin(AccessToken accessToken) throws Exception {
		twitter.setOAuthAccessToken(accessToken);
		startListener();
		logined = true;
		Log.i("TwitterManager", "Successfully logined in twitter!");
	}

	private void startListener() {
		new Thread("Twitter-Listener") {
			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(75000);
						try {
							Status status = twitter.getMentionsTimeline().get(0);
							if (status.getId() != lastMentionID) {
								if (lastMentionID != 0) {
									UiModule.getInstance().notify(TwitterManager.this, status.getUser().getName() + ": " + status.getText());
								}
								lastMentionID = status.getId();
								saveSettings();
							}
							connected = true;
						} catch (Exception err) {
							if (connected) {
								Log.e("TwitterManager", "Error in twitter listener. Trying to reconnect", err);
							} else {
								Log.e("TwitterManager", "Error in twitter listener. Trying to reconnect. " + err);
							}
							connected = false;
						}
					}
				} catch (InterruptedException ignored) {
				}
			}
		}.start();
	}

	private String[] loadSecrets() throws IOException {
		String[] result = new String[2];

		BufferedReader reader = new BufferedReader(new FileReader(secretsFile));
		String s;
		int i = 0;
		while ((s = reader.readLine()) != null) {
			switch (i) {
				case 0:
					result[0] = s;
					break;
				case 1:
					result[1] = s;
					break;
			}
			i++;
		}
		reader.close();

		return result;
	}

	private AccessToken loadAuth() throws IOException {
		String token = null;
		String tokenSecret = null;

		BufferedReader reader = new BufferedReader(new FileReader(authFile));
		String s;
		int i = 0;
		while ((s = reader.readLine()) != null) {
			switch (i) {
				case 0:
					token = s;
					break;
				case 1:
					tokenSecret = s;
					break;
			}
			i++;
		}
		reader.close();

		return new AccessToken(token, tokenSecret);
	}

	private void saveAuth(AccessToken accessToken) throws IOException {
		authFile.getParentFile().mkdirs();

		PrintWriter writer = new PrintWriter(authFile);
		writer.println(accessToken.getToken());
		writer.println(accessToken.getTokenSecret());
		writer.close();
	}

	private void loadSettings() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
		String s;
		int i = 0;
		while ((s = reader.readLine()) != null) {
			switch (i) {
				case 0:
					lastMentionID = Long.parseLong(s);
					break;
			}
			i++;
		}
		reader.close();
	}

	private void saveSettings() throws IOException {
		settingsFile.getParentFile().mkdirs();

		PrintWriter writer = new PrintWriter(settingsFile);
		writer.println(lastMentionID);
		writer.close();
	}

	@Override
	public boolean isLogined() {
		return logined;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public String getName() {
		return "tw";
	}
}
