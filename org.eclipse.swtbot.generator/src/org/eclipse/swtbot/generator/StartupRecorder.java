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
package org.eclipse.swtbot.generator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class StartupRecorder implements IStartup {

	public void earlyStartup() {
		final EventRecorder recorder = new EventRecorder();
		final CodeGenerator generator = new CodeGenerator();
		recorder.addListener(generator);

		final Display display = PlatformUI.getWorkbench().getDisplay();

		display.asyncExec(new Runnable() {
			public void run() {
				//display.addFilter(SWT.Activate, recorder);
				display.addFilter(SWT.Close, recorder);
				display.addFilter(SWT.MouseDown, recorder);
				display.addFilter(SWT.MouseDoubleClick, recorder);
				display.addFilter(SWT.MouseUp, recorder);
				display.addFilter(SWT.KeyDown, recorder);
				Shell recorderShell = new Shell(PlatformUI.getWorkbench().getDisplay(), SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
				recorderShell.setText("SWTBot test recorder");
				recorder.ignoreShell(recorderShell);
				new RecorderDialog(recorderShell, recorder, generator).open();
			}
		});
	}

}
