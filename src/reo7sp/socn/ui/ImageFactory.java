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

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * Created by reo7sp on 10/19/13 at 4:20 PM
 */
public class ImageFactory {
	private final Map<String, Image> cache = new HashMap<String, Image>();

	ImageFactory() {
	}

	synchronized Image getImage(String name) {
		Image image = cache.get(name);

		if (image == null) {
			if (cache.size() > 64) {
				cache.clear();
			}
			image = new ImageIcon(getClass().getResource("/reo7sp/socn/res/img/" + name + ".png")).getImage();
			cache.put(name, image);
		}

		return image;
	}
}
