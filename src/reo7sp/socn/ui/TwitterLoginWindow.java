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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import reo7sp.socn.Log;
import reo7sp.socn.social.TwitterManager;
import reo7sp.socn.ui.widget.FancyButton;
import reo7sp.socn.ui.widget.FancyLabel;
import reo7sp.socn.ui.widget.FancyTextBox;

/**
 * Created by reo7sp on 10/16/13 at 4:01 PM
 */
public class TwitterLoginWindow extends Window {
	@Override
	protected void initPanel() {
		panel.setLayout(null);

		final FancyLabel label = new FancyLabel();
		label.setBounds(250, 100, 300, 40);
		label.setText("Enter pin which is in browser");
		label.setHorizontalAlignment(FancyLabel.CENTER);
		panel.add(label);

		final FancyTextBox field = new FancyTextBox();
		field.setBounds(250, 150, 300, 40);
		panel.add(field);

		final FancyButton loginButton = new FancyButton();
		loginButton.setText("ОК");
		loginButton.setBounds(350, 250, 100, 35);
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					TwitterManager.getInstance().continueLogin(field.getText());
				} catch (Exception err) {
					Log.e("MainWindow", "Can't continue login in twitter", err);
				}
			}
		});
		panel.add(loginButton);
	}

	@Override
	void onVisibilityChange(boolean b) {
	}
}
