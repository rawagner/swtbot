/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

@Deprecated
public class EventRecorder implements Listener {

	public static interface RecorderEventListener {
		public void notifyRecorderEvent(EventRecorder recorder);
	}

	private boolean recording;
	private List<Event> allEvents = new ArrayList<Event>();
	private int currentEventIndex = 0;
	private Shell ignoredShell;
	private List<RecorderEventListener> listeners = new ArrayList<EventRecorder.RecorderEventListener>();

	public boolean isReording() {
		return this.recording;
	}

	public void switchRecording() {
		this.recording = !this.recording;

	}

	public void addListener(RecorderEventListener listener) {
		this.listeners.add(listener);
	}

	public void handleEvent(Event event) {
		if (!this.recording) {
			return;
		}
		this.allEvents.add(event);
		notifyListeners();
	}

	private void notifyListeners() {
		for (RecorderEventListener listener : this.listeners) {
			listener.notifyRecorderEvent(this);
		}
	}

	public void shiftCurrentIndex(int nbEvents) {
		this.currentEventIndex += nbEvents;
	}

	public List<Event> getAllEvents() {
		return this.allEvents; // TODO use an immutable copy
	}

	public List<Event> getToProcessEvents() {
		return this.allEvents.subList(this.currentEventIndex, this.allEvents.size());
	}

}
