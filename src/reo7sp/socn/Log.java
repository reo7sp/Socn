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

import java.awt.TrayIcon;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import reo7sp.socn.ui.UiModule;

/**
 * Created by reo7sp on 7/28/13 at 1:56 PM
 */
public class Log {
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

	public static void log(Level level, Object tag, Object message) {
		String s = "[" + dateFormat.format(new Date()) + "] [" + level + "] [" + tag + "] " + message;
		System.out.println(s);
		UiModule.getInstance().log(s);
	}

	public static void debug(Object tag, Object message) {
		log(Level.DEBUG, tag, message);
	}

	public static void info(Object tag, Object message) {
		log(Level.INFO, tag, message);
	}

	public static void warning(Object tag, Object message) {
		log(Level.WARNING, tag, message);
	}

	public static void error(Object tag, Object message) {
		log(Level.ERROR, tag, message);
	}

	public static void error(Object tag, Object message, Throwable e) {
		StringBuilder builder = new StringBuilder();
		builder.append(message).append("\n");
		builder.append("  ").append(e).append("\n");
		for (StackTraceElement element : e.getStackTrace()) {
			builder.append("    at ").append(element.toString()).append("\n");
		}
		if (e.getCause() != null) {
			builder.append("  Caused by ").append(e.getCause()).append("\n");
			for (StackTraceElement element : e.getCause().getStackTrace()) {
				builder.append("    at ").append(element.toString()).append("\n");
			}
		}
		Log.e(tag, builder.toString());
		UiModule.getInstance().showPopup("" + message, "" + e, TrayIcon.MessageType.ERROR);
	}

	public static void l(Level level, Object tag, Object message) {
		log(level, tag, message);
	}

	public static void d(Object tag, Object message) {
		log(Level.DEBUG, tag, message);
	}

	public static void i(Object tag, Object message) {
		log(Level.INFO, tag, message);
	}

	public static void w(Object tag, Object message) {
		log(Level.WARNING, tag, message);
	}

	public static void e(Object tag, Object message) {
		log(Level.ERROR, tag, message);
	}

	public static void e(Object tag, Object message, Throwable e) {
		error(tag, message, e);
	}

	public static enum Level {
		DEBUG,
		INFO,
		WARNING,
		ERROR,
	}
}
