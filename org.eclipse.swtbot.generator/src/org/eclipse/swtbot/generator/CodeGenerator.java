package org.eclipse.swtbot.generator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.generator.EventRecorder.RecorderEventListener;

public class CodeGenerator implements RecorderEventListener {

	public static interface CodeGenerationListener {
		public void handleCodeGenerated(String code);
	}

	private List<CodeGenerationListener> listeners = new ArrayList<CodeGenerationListener>();

	public void notifyRecorderEvent(EventRecorder recorder) {
		List<Event> toProcessEvents = recorder.getToProcessEvents();
		// search patterns/sequences/"strings" in toProcessEvents. Use Hamcrest matchers?
		if (toProcessEvents.size() >= 2 && toProcessEvents.get(0).type == SWT.MouseDown && toProcessEvents.get(1).type == SWT.MouseUp) {
			generateCode(toProcessEvents.get(0).widget, "click");
			recorder.shiftCurrentIndex(2);
		}
	}

	private void generateCode(Widget widget, String action) {
		StringBuilder code = new StringBuilder();
		if (! (widget instanceof Control)) {
			code.append("// Only Controls are supported, here we have a ");
			code.append(widget.getClass().getSimpleName());
			code.append("\n");
		}
		Control control = (Control) widget;
		code.append("bot.");
		code.append(widget.getClass().getSimpleName());
		code.append("(\"");
		try {
			Method getText = control.getClass().getMethod("getText");
			String text = (String) getText.invoke(control);
			code.append(text);
		} catch (Exception ex) {
			code.append("COULD NOT GET TEXT");
		}
		code.append("\").");
		code.append(action);
		code.append("();\n");
		notifyCodeCreated(code.toString());
	}

	private void notifyCodeCreated(String code) {
		for (CodeGenerationListener listener : this.listeners) {
			listener.handleCodeGenerated(code);
		}
	}

	public void addListener(CodeGenerationListener listener) {
		this.listeners.add(listener);
	}

}
