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
import java.io.IOException;
import java.net.URL;

import reo7sp.socn.Core;
import reo7sp.socn.Log;
import reo7sp.socn.Module;
import reo7sp.socn.Utils;

/**
 * Created by reo7sp on 12/1/13 at 7:12 PM
 */
public class UpdateModule extends Module {
	private static final UpdateModule INSTANCE = new UpdateModule();
	private static final String URL_ROOT = null;

	private UpdateModule() {
	}

	public static UpdateModule getInstance() {
		return INSTANCE;
	}

	@Override
	public void enable() throws Exception {
		if (URL_ROOT == null) {
			return;
		}
		new Thread("Updater") {
			@Override
			public void run() {
				try {
					updateCore();
				} catch (IOException err) {
					Log.e("UpdateModule", "Can't update", err);
				}
			}
		}.start();
	}

	@Override
	public void disable() throws Exception {

	}

	private void updateCore() throws IOException {
		final File file = new File(Core.getFolderLocation() + File.separator + "Socn.jar");
		final String md5Local = Utils.getChecksum(file);
		final String md5Net = Utils.loadFromNet(URL_ROOT + "Socn.jar.md5").trim();

		Log.i("UpdateModule", "Local: " + md5Local + ", Net: " + md5Net);
		if (!md5Local.equals(md5Net)) {
			new Download(new URL(URL_ROOT + "/Socn.jar"), file).download();
		}
	}
}
