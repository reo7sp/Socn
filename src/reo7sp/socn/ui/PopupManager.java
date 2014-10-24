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
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by reo7sp on 12/3/13 at 7:32 PM
 */
public class PopupManager {
	private final Collection<Popup> popups = new HashSet<Popup>();

	PopupManager() {
	}

	void sendMessage(String title, String content, TrayIcon.MessageType type) {
		Popup popup = new Popup(title, content, type);
		if (popups.add(popup)) {
			UiModule.getInstance().displayMessage(title, content, type);
		}
	}

	private class Popup {
		public String title;
		public String content;
		public TrayIcon.MessageType type;

		private Popup(String title, String content, TrayIcon.MessageType type) {
			this.title = title;
			this.content = content;
			this.type = type;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Popup)) {
				return false;
			}

			Popup popup = (Popup) o;

			if (!content.equals(popup.content)) {
				return false;
			}
			if (!title.equals(popup.title)) {
				return false;
			}
			if (type != popup.type) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = title.hashCode();
			result = 31 * result + content.hashCode();
			result = 31 * result + type.hashCode();
			return result;
		}
	}
}
