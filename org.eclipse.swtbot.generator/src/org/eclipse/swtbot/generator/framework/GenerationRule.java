package org.eclipse.swtbot.generator.framework;

import org.eclipse.swt.widgets.Event;

public abstract class GenerationRule {

	public abstract boolean appliesTo(Event event);

	public abstract void initializeForEvent(Event event) ;

	public String generateCode() {
		return getWidgetAccessor() + getActon();
	}

	protected abstract String getWidgetAccessor();
	protected abstract String getActon();

}
