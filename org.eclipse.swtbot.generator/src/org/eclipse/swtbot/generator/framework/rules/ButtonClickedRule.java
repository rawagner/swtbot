package org.eclipse.swtbot.generator.framework.rules;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ButtonClickedRule extends GenerationRule {

	private String buttonText;
	private int index;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Button && event.type == SWT.Selection;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.buttonText = ((Button)event.widget).getText();
		if (buttonText == null) {
			this.index = WidgetUtils.getIndex(event.widget);
		}
	}

	@Override
	protected String getWidgetAccessor() {
		if (this.buttonText != null) {
			return "bot.button(\"" + this.buttonText + "\")";
		} else {
			return "bot.button(" + this.index + ")";
		}
	}

	@Override
	protected String getActon() {
		return ".click()";
	}

}
