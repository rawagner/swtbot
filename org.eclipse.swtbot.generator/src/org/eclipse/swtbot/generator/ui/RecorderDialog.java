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

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
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
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher.CodeGenerationListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.omg.CORBA.StructMember;

public class RecorderDialog extends TitleAreaDialog {

	private BotGeneratorEventDispatcher recorder;
	private List<Generator> availableGenerators;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public RecorderDialog(Shell parentShell, BotGeneratorEventDispatcher recorder, List<Generator> availableGenerators) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE | SWT.MAX);
		setBlockOnOpen(false);
		this.recorder = recorder;
		this.availableGenerators = availableGenerators;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("SWTBot Test Recorder");
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(new GridLayout(1, false));

		Composite generatorSelectionContainer = new Composite(container, SWT.NONE);
		generatorSelectionContainer.setLayout(new RowLayout());
		new Label(generatorSelectionContainer, SWT.NONE).setText("Target Bot API:");
		ComboViewer comboViewer = new ComboViewer(generatorSelectionContainer);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object o) {
				return ((Generator)o).getLabel();
			}
		});
		comboViewer.setInput(this.availableGenerators);
		comboViewer.setSelection(new StructuredSelection(this.recorder.getCurrentGenerator()));
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				Generator newGenerator = (Generator) ((IStructuredSelection)event.getSelection()).getFirstElement();
				recorder.setGenerator(newGenerator);
			}
		});

		final Text generatedCode = new Text(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		generatedCode.setText("TODO: generated code\n");
		generatedCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Composite actionsComposite = new Composite(container, SWT.NONE);
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
		this.recorder.addListener(new CodeGenerationListener() {
			public void handleCodeGenerated(String code) {
				generatedCode.setText(generatedCode.getText() + code + ";\n");
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

		return container;
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
