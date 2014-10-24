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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import reo7sp.socn.Log;
import reo7sp.socn.Utils;
import reo7sp.socn.social.SocialNetwork;
import reo7sp.socn.social.TwitterManager;
import reo7sp.socn.social.VkManager;
import reo7sp.socn.ui.widget.FancyButton;
import reo7sp.socn.ui.widget.FancyLabel;

/**
 * Created by reo7sp on 10/19/13 at 4:13 PM
 */
public class MainWindow extends Window {
	private final FancyLabel label = new FancyLabel();
	private final FancyButton loginVkButton = new FancyButton();
	private final FancyButton loginTwButton = new FancyButton();
	private final JTextArea twMessagesArea = new JTextArea();
	private final JTextArea vkMessagesArea = new JTextArea();
	private final JTextArea logTextArea = new JTextArea();

	@Override
	protected void initPanel() {
		panel.setLayout(null);

		label.setBounds(50, 15, 400, 35);
		label.setText("Messages:");
		panel.add(label);

		loginTwButton.setBounds(440, 10, 150, 35);
		loginTwButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (TwitterManager.getInstance().isLogined()) {
					TwitterManager.getInstance().disconnect();
				} else {
					try {
						TwitterManager.getInstance().startLogin();
					} catch (Exception err) {
						Log.e("MainWindow", "Can't start login in twitter", err);
					}
				}
				updateButtons();
			}
		});
		panel.add(loginTwButton);

		loginVkButton.setBounds(600, 10, 150, 35);
		loginVkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (VkManager.getInstance().isLogined()) {
					VkManager.getInstance().disconnect();
				} else {
					try {
						VkManager.getInstance().startLogin();
					} catch (Exception err) {
						Log.e("MainWindow", "Can't start login in vk", err);
					}
				}
				updateButtons();
			}
		});
		panel.add(loginVkButton);

		JScrollPane twMessagesContainer = new JScrollPane(twMessagesArea);
		twMessagesContainer.setBounds(50, 75, 700, 75);
		twMessagesContainer.setBorder(new EmptyBorder(8, 8, 8, 8));
		twMessagesArea.setOpaque(false);
		twMessagesArea.setEditable(false);
		panel.add(twMessagesContainer);

		JScrollPane vkMessagesContainer = new JScrollPane(vkMessagesArea);
		vkMessagesContainer.setBounds(50, 175, 700, 75);
		vkMessagesContainer.setBorder(new EmptyBorder(8, 8, 8, 8));
		vkMessagesArea.setOpaque(false);
		vkMessagesArea.setEditable(false);
		panel.add(vkMessagesContainer);

		JScrollPane logTextContainer = new JScrollPane(logTextArea);
		logTextContainer.setBounds(50, 275, 700, 275);
		logTextContainer.setBorder(new EmptyBorder(8, 8, 8, 8));
		logTextArea.setOpaque(false);
		logTextArea.setEditable(false);
		panel.add(logTextContainer);

		JLabel twIcon = new JLabel(new ImageIcon(UiModule.getInstance().getImageFactory().getImage("tw")));
		twIcon.setBounds(730, 85, 64, 64);
		twIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Utils.openBrowser("https://twitter.com/i/connect");
				} catch (Exception err) {
					Log.e("MainWindow", "Can't open tw page", err);
				}
			}
		});
		panel.add(twIcon);

		JLabel vkIcon = new JLabel(new ImageIcon(UiModule.getInstance().getImageFactory().getImage("vk")));
		vkIcon.setBounds(730, 185, 64, 64);
		vkIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Utils.openBrowser("http://vk.com/im");
				} catch (Exception err) {
					Log.e("MainWindow", "Can't open vk page", err);
				}
			}
		});
		panel.add(vkIcon);
	}

	@Override
	void onVisibilityChange(boolean b) {
		if (b) {
			updateButtons();
		}
	}

	private void updateButtons() {
		loginTwButton.setText(TwitterManager.getInstance().isLogined() ? "Disconnect from TW" : "Login in TW");
		loginVkButton.setText(VkManager.getInstance().isLogined() ? "Disconnect from VK" : "Login in VK");
	}

	void log(String s) {
		logTextArea.append(s + "\n");
	}

	void updateMessages(SocialNetwork network, String content) {
		if (network instanceof TwitterManager) {
			twMessagesArea.setText(content);
		} else if (network instanceof VkManager) {
			vkMessagesArea.setText(content);
		}
	}
}
