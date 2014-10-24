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

import reo7sp.socn.social.SocialModule;
import reo7sp.socn.ui.UiModule;
import reo7sp.socn.update.UpdateModule;

/**
 * Created by reo7sp on 10/19/13 at 4:37 PM
 */
public class ModuleManager {
	private static final ModuleManager INSTANCE = new ModuleManager();
	private static final Module[] modules = {
			SocialModule.getInstance(),
			UiModule.getInstance(),
			UpdateModule.getInstance(),
	};

	static ModuleManager getInstance() {
		return INSTANCE;
	}

	synchronized void enableAll() throws Exception {
		for (Module module : modules) {
			Log.i("ModuleManager", "Enabling module " + module.getClass().getSimpleName());
			module.enable();
			Log.i("ModuleManager", "Module " + module.getClass().getSimpleName() + " enabled!");
		}
	}

	synchronized void disableAll() throws Exception {
		for (Module module : modules) {
			Log.i("ModuleManager", "Disabling module " + module.getClass().getSimpleName());
			module.disable();
			Log.i("ModuleManager", "Module " + module.getClass().getSimpleName() + " disabled!");
		}
	}
}
