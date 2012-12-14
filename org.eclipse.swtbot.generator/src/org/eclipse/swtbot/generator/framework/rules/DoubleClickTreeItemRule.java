package org.eclipse.swtbot.generator.framework.rules;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

public class DoubleClickTreeItemRule extends AbstractTreeGenerationRule {

	@Override
	public boolean appliesTo(Event event) {
		return super.appliesTo(event) && event.type == SWT.MouseDoubleClick;
	}

	@Override
	protected String getActon() {
		return ".doubleClick()";
	}

}
