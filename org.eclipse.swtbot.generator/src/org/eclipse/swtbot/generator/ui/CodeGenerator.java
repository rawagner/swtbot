package org.eclipse.swtbot.generator.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.generator.ui.EventRecorder.RecorderEventListener;

@Deprecated
public class CodeGenerator implements RecorderEventListener {



	private static class SimplifiedEvent {

		private Widget widget;
		private int eventType;

		public SimplifiedEvent(Widget widget, int eventType) {
			this.widget = widget;
			this.eventType = eventType;
		}

		public boolean looksLike(Event e) {
			return e.widget == this.widget && e.type == this.eventType;
		}
	}

	private Widget lastWidget;
	private String lastWidgetAccessor;
	private SimplifiedEvent ignoredEvent;


	public void notifyRecorderEvent(EventRecorder recorder) {
		List<Event> toProcessEvents = recorder.getToProcessEvents();
		if (toProcessEvents.size() == 0) {
			return;
		}
		if (this.ignoredEvent != null && this.ignoredEvent.looksLike(toProcessEvents.get(0))) {
			recorder.shiftCurrentIndex(1);
			this.ignoredEvent = null;
			return;
		}
		ignoredEvent = null;

		if (toProcessEvents.size() == 1) {
			// Accessor has to be created before widget get altered (eg Text change)
			// Is it only a workaround for Text?
			this.lastWidgetAccessor = createWidgetAccessor(toProcessEvents.get(0).widget);
		}
		boolean somethingHappened = false;
		// search patterns/sequences/"strings" in toProcessEvents. Use Hamcrest matchers?
		// replaced by the selectionEvent
		if (toProcessEvents.size() >= 3 &&
			toProcessEvents.get(0).type == SWT.MouseDown &&
			toProcessEvents.get(1).type == SWT.Selection &&
			toProcessEvents.get(2).type == SWT.MouseUp &&
			toProcessEvents.get(0).widget == toProcessEvents.get(1).widget &&
			toProcessEvents.get(2).widget == toProcessEvents.get(0).widget) {
			this.lastWidgetAccessor = createWidgetAccessor(toProcessEvents.get(1).item);
			generateCode("click()");
			recorder.shiftCurrentIndex(3);
			somethingHappened = true;
		} else if (toProcessEvents.size() >= 3 &&
			toProcessEvents.get(0).type == SWT.MouseDown &&
			toProcessEvents.get(1).type == SWT.Expand &&
			toProcessEvents.get(2).type == SWT.MouseUp &&
			toProcessEvents.get(0).widget == toProcessEvents.get(1).widget &&
			toProcessEvents.get(2).widget == toProcessEvents.get(0).widget) {
			this.lastWidgetAccessor = createWidgetAccessor(toProcessEvents.get(1).item);
			generateCode("expand()");
			recorder.shiftCurrentIndex(3);
			somethingHappened = true;
		} else 	if (toProcessEvents.size() >= 2 &&
			toProcessEvents.get(0).type == SWT.MouseDown &&
			toProcessEvents.get(1).type == SWT.MouseUp &&
			toProcessEvents.get(0).widget == toProcessEvents.get(1).widget) {
			generateCode("click()");
			recorder.shiftCurrentIndex(2);
			this.ignoredEvent = new SimplifiedEvent(toProcessEvents.get(0).widget, SWT.Selection);
			somethingHappened = true;
		} else if (/* Matching KeyPressed* */
				toProcessEvents.get(0).type == SWT.KeyDown &&
				toProcessEvents.get(toProcessEvents.size() - 1).type != SWT.KeyDown &&
				toProcessEvents.get(0).widget instanceof Text) {
			int i = 0;
			Text widget = (Text) toProcessEvents.get(0).widget;
			while (toProcessEvents.get(i).type == SWT.KeyDown && toProcessEvents.get(i).widget == widget) i++;
			generateCode("setText(\"" + widget.getText() + "\")");
			recorder.shiftCurrentIndex(i);
			somethingHappened = true;
		} else if (toProcessEvents.get(0).type == SWT.Selection && toProcessEvents.get(0).widget instanceof Button) {
			generateCode("click()");
			recorder.shiftCurrentIndex(1);
			somethingHappened = true;
		} else if (toProcessEvents.get(0).type == SWT.Selection && ! (toProcessEvents.get(0).widget instanceof Button)) {
			generateCode("select()");
			recorder.shiftCurrentIndex(1);
			somethingHappened = true;
		} else if (toProcessEvents.get(0).type == SWT.Expand) {
			generateCode("expand()");
			recorder.shiftCurrentIndex(1);
			somethingHappened = true;
		}
		if (somethingHappened) {
			notifyRecorderEvent(recorder);
		}
	}

	private void generateCode(String action) {
		StringBuilder code = new StringBuilder();
		code.append(lastWidgetAccessor);
		code.append(".");
		code.append(action);
		code.append(";\n");
		//notifyCodeCreated(code.toString());
	}

	private static String createWidgetAccessor(Widget widget) {
		StringBuilder code = new StringBuilder();
		if (widget instanceof Control) {
			Control control = (Control) widget;
			code.append("bot.");
			String botMethod = widget.getClass().getSimpleName();
			code.append(Character.toLowerCase(botMethod.charAt(0)));
			code.append(botMethod.substring(1));
			code.append("(\"");
			try {
				Method getText = control.getClass().getMethod("getText");
				String text = (String) getText.invoke(control);
				code.append(text);
			} catch (Exception ex) {
				code.append("COULD NOT GET TEXT");
			}
			code.append("\")");
		} else if (widget instanceof MenuItem) {
			MenuItem item = (MenuItem)widget;
			List<String> path = new ArrayList<String>();
			path.add(item.getText());
			Menu parent = null;
			while (item != null && (parent = item.getParent()) != null) {
				item = parent.getParentItem();
				if (item != null && item.getText() != null) {
					path.add(item.getText());
				}
			}
			Collections.reverse(path);
			code.append("bot");
			for (String text : path) {
				code.append(".menu(\"");
				code.append(text);
				code.append("\")");
			}
		} else if (widget instanceof TreeItem) {
			TreeItem item = (TreeItem) widget;
			code.append("bot.tree().getItem(\"" + item.getText() + "\")");
		}

		return code.toString();
	}


}
