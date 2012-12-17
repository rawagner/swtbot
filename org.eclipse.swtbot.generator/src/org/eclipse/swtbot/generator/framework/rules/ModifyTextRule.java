package org.eclipse.swtbot.generator.framework.rules;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ModifyTextRule extends GenerationRule {

	private Text text;
	private String newValue;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Text && event.type == SWT.Modify;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.text = (Text)event.widget;
		this.newValue = this.text.getText();;
	}

	@Override
	protected String getWidgetAccessor() {
		StringBuilder res = new StringBuilder();
		res.append("bot.text(");
		int index = WidgetUtils.getIndex(this.text);
		if (index != 0) {
			res.append(index);
		}
		res.append(")");
		return res.toString();
	}

	@Override
	protected String getActon() {
		return ".setText(\"" + this.newValue + "\")";
	}

}
