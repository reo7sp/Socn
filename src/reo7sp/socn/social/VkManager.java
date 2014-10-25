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
import java.nio.charset.Charset;

import reo7sp.socn.Core;
import reo7sp.socn.Log;
import reo7sp.socn.Utils;
import reo7sp.socn.ui.MainWindow;
import reo7sp.socn.ui.UiModule;
import reo7sp.socn.ui.VkLoginWindow;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * Created by reo7sp on 10/19/13 at 8:37 PM
 */
public class VkManager extends SocialNetwork {
	private static final VkManager INSTANCE = new VkManager();
	private final File authFile = new File(Core.getFolderLocation() + File.separator + "vk.auth");
	private final File secretsFile = new File(Core.getFolderLocation() + File.separator + "vk.secret");
	private String accessToken;
	private String secret;
	private boolean logined;
	private boolean connected = true;

	private VkManager() {
		try {
			loadSecret();
		} catch (Exception err) {
			Log.w("VkManager", "Can't load secret " + err);
		}
	}

	public static VkManager getInstance() {
		return INSTANCE;
	}

	@Override
	public void fastLogin() {
		try {
			loadAuth();
			endLogin();
		} catch (Exception ignored) {
		}
	}

	@Override
	public void disconnect() {
		accessToken = null;
		authFile.delete();
		logined = false;
		Log.i("VkManager", "Successfully disconnected from vk!");
	}

	public void startLogin() throws Exception {
		accessToken = null;
		UiModule.getInstance().setVisibleWindow(VkLoginWindow.class);
		Utils.openBrowser("https://oauth.vk.com/authorize?" +
				"client_id=" + secret + "&" +
				"scope=messages,offline&" +
				"redirect_uri=https://oauth.vk.com/blank.html&" +
				"display=page&" +
				"v=5.3&" +
				"revoke=1&" +
				"response_type=token");
	}

	public void continueLogin(String accessToken) throws Exception {
		if (accessToken == null || accessToken.isEmpty()) {
			return;
		}
		this.accessToken = accessToken;
		saveAuth();
		endLogin();
		UiModule.getInstance().setVisibleWindow(MainWindow.class);
	}

	private void endLogin() throws Exception {
		if (accessToken == null) {
			return;
		}
		startListener();
		logined = true;
		Log.i("VkManager", "Successfully logined in vk!");
	}

	private void startListener() {
		new Thread("VK-Listener") {
			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(15000);
						try {
							JSONObject messageGetResponse = invokeApiMethod("messages.get", "out=0", "offset=0", "filters=1");
							if (messageGetResponse.has("error")) {
								Log.w("VkManager", messageGetResponse.getJSONObject("error"));
							} else {
								JSONArray messages = messageGetResponse.getJSONObject("response").getJSONArray("items");
								UiModule.getInstance().clearNotifications(VkManager.this);
								for (int i = 0, count = messages.length(); i < count; i++) {
									JSONObject message = messages.getJSONObject(i);
									String name = null;
									try {
										JSONObject usersGetResponse = invokeApiMethod("users.get", "user_ids=" + message.getString("user_id"));
										JSONObject user = usersGetResponse.getJSONArray("response").getJSONObject(0);
										name = user.getString("first_name") + " " + user.getString("last_name");
									} catch (Exception ignored) {
									}
									UiModule.getInstance().notify(VkManager.this, name + ": " + message.getString("body"));
								}
							}
							connected = true;
						} catch (Exception err) {
							if (connected) {
								Log.e("VkManager", "Error in vk listener. Trying to reconnect", err);
							} else {
								Log.e("VkManager", "Error in vk listener. Trying to reconnect. " + err);
							}
							connected = false;
						}
					}
				} catch (InterruptedException ignored) {
				}
			}
		}.start();
	}

	private JSONObject invokeApiMethod(String method, String firstParam, String... otherParams) throws IOException, JSONException {
		String params = firstParam;
		for (String s : otherParams) {
			params += "&" + s;
		}
		String response = Utils.loadFromNet("https://api.vk.com/method/" + method + "?" + params + "&v=5.3&access_token=" + accessToken);
		response = new String(response.getBytes(Charset.forName("UTF-8")), Charset.defaultCharset());
		return new JSONObject(response);
	}

	private void loadSecret() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(secretsFile));
		String s;
		int i = 0;
		while ((s = reader.readLine()) != null) {
			switch (i) {
				case 0:
					secret = s;
					break;
			}
			i++;
		}
		reader.close();
	}

	private void loadAuth() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(authFile));
		String s;
		int i = 0;
		while ((s = reader.readLine()) != null) {
			switch (i) {
				case 0:
					accessToken = s;
					break;
			}
			i++;
		}
		reader.close();
	}

	private void saveAuth() throws IOException {
		authFile.getParentFile().mkdirs();

		PrintWriter writer = new PrintWriter(authFile);
		writer.println(accessToken);
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
		return "vk";
	}
}
