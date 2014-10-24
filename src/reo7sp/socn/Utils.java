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

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;

/**
 * Created by reo7sp on 10/19/13 at 8:43 PM
 */
public class Utils {
	private Utils() {
	}

	public static void openBrowser(String url) throws URISyntaxException, IOException {
		Desktop.getDesktop().browse(new URL(url).toURI());
	}

	public static String loadFromNet(String url) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));
		String s;
		while ((s = reader.readLine()) != null) {
			result.append(s).append("\n");
		}
		reader.close();
		return result.toString();
	}

	public static String getChecksum(File file) {
		if (!file.exists() || !file.isFile()) {
			return "";
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);
			byte[] dataBytes = new byte[1024];

			int x;
			while ((x = fis.read(dataBytes)) != -1) {
				messageDigest.update(dataBytes, 0, x);
			}
			byte[] messageDigestBytes = messageDigest.digest();

			// convert the byte to hex format
			StringBuilder checksum = new StringBuilder();
			for (byte messageDigestByte : messageDigestBytes) {
				checksum.append(Integer.toString((messageDigestByte & 0xff) + 0x100, 16).substring(1));
			}
			return checksum.toString();
		} catch (Exception ignored) {
			return "";
		}
	}
}
