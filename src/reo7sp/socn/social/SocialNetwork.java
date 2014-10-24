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

package reo7sp.socn.social;

/**
 * Created by reo7sp on 10/19/13 at 6:14 PM
 */
public abstract class SocialNetwork {
	public abstract void fastLogin();

	public abstract void disconnect();

	public abstract boolean isLogined();

	public abstract boolean isConnected();

	public abstract String getName();

	public String toString() {
		return getName();
	}
}
