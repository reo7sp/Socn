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

import javax.swing.JFrame;

import reo7sp.socn.Core;
import reo7sp.socn.ui.widget.FancyPanel;

/**
 * Created by reo7sp on 10/19/13 at 6:09 PM
 */
public abstract class Window {
	protected final JFrame frame = new JFrame();
	protected final FancyPanel panel = new FancyPanel();
	private boolean initialized;

	Window() {
	}

	protected void initFrame() {
		frame.setTitle(Core.NAME);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setResizable(false);

		initPanel();
		frame.setContentPane(panel);
	}

	protected abstract void initPanel();

	abstract void onVisibilityChange(boolean b);

	boolean isVisible() {
		return frame.isVisible();
	}

	void setVisible(boolean b) {
		if (!initialized) {
			initFrame();
			initialized = true;
		}
		frame.setVisible(b);
		onVisibilityChange(b);
	}
}
