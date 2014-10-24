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

package reo7sp.socn.ui.widget;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Created by reo7sp on 9/10/13 at 4:48 PM
 */
public class FancyButton extends JButton {
	public FancyButton() {
		setForeground(Color.WHITE);
		setOpaque(false);
		setBackground(Color.BLACK);
		setFocusPainted(false);
		setFont(new Font(null, Font.PLAIN, 12));
		setBorder(new CompoundBorder(new LineBorder(Color.WHITE), new EmptyBorder(8, 8, 8, 8)));
	}
}
