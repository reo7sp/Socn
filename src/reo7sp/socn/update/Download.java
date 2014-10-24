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

package reo7sp.socn.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import reo7sp.socn.Log;

/**
 * Created by reo7sp on 12/1/13 at 7:13 PM
 */
public class Download {
	private final URL url;
	private final File file;

	public Download(URL url, File file) {
		this.url = url;
		this.file = file;
	}

	public void download() throws IOException {
		Log.i("Download", "Downloading...");

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}

		ReadableByteChannel in = Channels.newChannel(url.openStream());
		FileOutputStream out = new FileOutputStream(file);
		out.getChannel().transferFrom(in, 0, Long.MAX_VALUE);
		out.close();
		in.close();

		Log.i("Download", "Downloaded!");
	}
}
