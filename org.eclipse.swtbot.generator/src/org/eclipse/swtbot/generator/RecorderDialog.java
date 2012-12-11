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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.generator.CodeGenerator.CodeGenerationListener;
import org.eclipse.swtbot.generator.EventRecorder.RecorderEventListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class RecorderDialog extends TitleAreaDialog {

	private EventRecorder recorder;
	private CodeGenerator generator;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public RecorderDialog(Shell parentShell, EventRecorder recorder, CodeGenerator generator) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE | SWT.MAX);
		setBlockOnOpen(false);
		this.recorder = recorder;
		this.generator = generator;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("SWTBot Test Recorder");
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		SashForm container = new SashForm(area, SWT.VERTICAL);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite eventComposite = new Composite(container, SWT.NONE);
		eventComposite.setLayout(new GridLayout(1,false));
		new Label(eventComposite, SWT.NONE).setText("Events");
		SashForm eventsSash = new SashForm(eventComposite, SWT.HORIZONTAL);
		eventsSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final ListViewer eventsList = new ListViewer(eventsSash);
		eventsList.setContentProvider(new ArrayContentProvider());
		new Label(eventsSash, SWT.NONE).setText("TODO: Details");

		Composite generatorComposite = new Composite(container, SWT.NONE);
		generatorComposite.setLayout(new GridLayout(1, false));
		final Text generatedCode = new Text(generatorComposite, SWT.MULTI);
		generatedCode.setText("TODO: generated code\n");
		generatedCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Composite actionsComposite = new Composite(generatorComposite, SWT.NONE);
		actionsComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

		final Button recordPauseButton = new Button(actionsComposite, SWT.PUSH);
		recordPauseButton.setText(this.recorder.isReording() ? "Pause" : "Start Recording");
		final Button copyButton = new Button(actionsComposite, SWT.PUSH);
		copyButton.setToolTipText("Copy");
		copyButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_COPY));

		recordPauseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				recorder.switchRecording();
				recordPauseButton.setText(recorder.isReording() ? "Pause" : "Start Recording");
			}
		});
		eventsList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				updateDetailsForEvent(((IStructuredSelection)event.getSelection()).getFirstElement());
			}
		});
		this.recorder.addListener(new RecorderEventListener() {
			public void notifyRecorderEvent(EventRecorder recorder) {
				eventsList.setInput(recorder.getAllEvents());
			}
		});
		this.generator.addListener(new CodeGenerationListener() {
			public void handleCodeGenerated(String code) {
				generatedCode.setText(generatedCode.getText() + code);
			}
		});
		copyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final Clipboard cb = new Clipboard(Display.getCurrent());
				TextTransfer textTransfer = TextTransfer.getInstance();
			    cb.setContents(new Object[] { generatedCode.getText() }, new Transfer[] { textTransfer });
			    cb.dispose();
			}
		});

		return area;
	}

	protected void updateDetailsForEvent(Object firstElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createButtonsForButtonBar(Composite parent) {
		// Override to remove default buttons
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 650);
	}
}
