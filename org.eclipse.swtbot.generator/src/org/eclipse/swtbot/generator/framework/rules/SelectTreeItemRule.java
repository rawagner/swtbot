package org.eclipse.swtbot.generator.framework.rules;

import org.eclipse.swt.widgets.Event;

import org.eclipse.swt.SWT;

public class SelectTreeItemRule extends AbstractTreeGenerationRule {

	@Override
	public boolean appliesTo(Event e) {
		return super.appliesTo(e) && e.type == SWT.Selection;
	}

	@Override
	protected String getActon() {
		return ".click()";
	}

}
