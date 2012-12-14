/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *    Rasta TODO (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.Generator;

public class BotGeneratorEventDispatcher implements Listener {

	public static interface CodeGenerationListener {
		public void handleCodeGenerated(String code);
	}

	private Generator generator;
	private List<GenerationRule> generationRules;
	private List<CodeGenerationListener> listeners = new ArrayList<CodeGenerationListener>();
	private Shell ignoredShell;
	private boolean recording;

	public void setGenerator(Generator generator)  {
		this.generator = generator;
		this.generationRules = generator.createRules();
	}

	public void handleEvent(Event event) {
		if (this.ignoredShell != null && event.widget instanceof Control && this.ignoredShell.equals(getShell((Control)event.widget))) {
			return;
		}
		for (GenerationRule rule : generationRules) {
			if (rule.appliesTo(event)) {
				rule.initializeForEvent(event);
				//rule.setPreviousEvent()
				dispatchCodeGenerated(rule.generateCode());
			}
		}
	}

	private static Shell getShell(Control widget) {
		while (widget != null) {
			if (widget.getParent() == null) {
				return (Shell)widget;
			} else {
				widget = ((Control)widget).getParent();
			}
		}
		return null;
	}


	private void dispatchCodeGenerated(String code) {
		for (CodeGenerationListener listener : this.listeners) {
			listener.handleCodeGenerated(code);
		}
	}

	public void addListener(CodeGenerationListener listener) {
		this.listeners.add(listener);
	}

	public void ignoreShell(Shell shell) {
		this.ignoredShell = shell;
	}

	public boolean isReording() {
		return this.recording;
	}

	public void switchRecording() {
		this.recording = !this.recording;
	}

	public Generator getCurrentGenerator() {
		return this.generator;
	}

}
