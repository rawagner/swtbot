package org.eclipse.swtbot.generator.framework.rules;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

public class ExpandTreeItemRule extends AbstractTreeGenerationRule {

	@Override
	public boolean appliesTo(Event e) {
		return super.appliesTo(e) && e.type == SWT.Expand;
	}

	@Override
	protected String getActon() {
		return ".expand()";
	}

}
