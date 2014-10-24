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

package reo7sp.socn;

import java.io.File;

/**
 * Created by reo7sp on 10/19/13 at 4:07 PM
 */
public class Core {
	private static OS os;
	private static String folderLocation;
	public static final String VERSION = "1.0.3";
	public static final String NAME = "Socn " + VERSION + " by Reo_SP";

	public static void main(String[] args) throws Exception {
		detectOS();
		findHomeFolder(getValueFromArgs(args, "dir"));
		ModuleManager.getInstance().enableAll();
	}

	private static void detectOS() {
		String systemName = System.getProperty("os.name").toLowerCase();
		if (systemName.contains("win")) {
			os = OS.WINDOWS;
		} else if (systemName.contains("linux")) {
			os = OS.LINUX;
		} else if (systemName.contains("mac")) {
			os = OS.MAC;
		}
	}

	private static String getValueFromArgs(String[] args, String key) {
		for (String arg : args) {
			String[] parts = arg.split("=", 2);
			if (parts[0].equalsIgnoreCase("--" + key)) {
				return parts[1];
			}
		}
		return null;
	}

	private static void findHomeFolder(String custom) {
		if (custom == null) {
			custom = ".Socn";
		}
		folderLocation = (os == OS.WINDOWS ? System.getenv("APPDATA") : System.getProperty("user.home")) + File.separator + custom;
	}

	public static OS getOS() {
		return os;
	}

	public static String getFolderLocation() {
		return folderLocation;
	}

	public static void shutdown() {
		try {
			ModuleManager.getInstance().disableAll();

			System.exit(0);
		} catch (Exception err) {
			Log.e("Core", "Can't shutdown", err);

			System.exit(1);
		}
	}
}
