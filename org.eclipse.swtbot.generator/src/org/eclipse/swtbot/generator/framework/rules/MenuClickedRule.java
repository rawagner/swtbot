package org.eclipse.swtbot.generator.framework.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.generator.framework.GenerationRule;

public class MenuClickedRule extends GenerationRule {

	private MenuItem item;

	@Override
	public boolean appliesTo(Event event) {
		return event.type == SWT.Selection && event.widget instanceof MenuItem;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.item = (MenuItem)event.widget;
	}

	@Override
	protected String getWidgetAccessor() {
		StringBuilder code = new StringBuilder();
		List<String> path = new ArrayList<String>();
		path.add(this.item.getText());
		MenuItem currentItem = this.item;
		Menu parent = null;
		while (currentItem != null && (parent = currentItem.getParent()) != null) {
			currentItem = parent.getParentItem();
			if (currentItem != null && currentItem.getText() != null) {
				path.add(currentItem.getText());
			}
		}
		Collections.reverse(path);
		code.append("bot");
		for (String text : path) {
			code.append(".menu(\"");
			code.append(text);
			code.append("\")");
		}
		return code.toString();
	}

	@Override
	protected String getActon() {
		return ".click()";
	}

}
