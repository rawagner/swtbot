package org.eclipse.swtbot.generator.framework.rules;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public abstract class AbstractTreeGenerationRule extends GenerationRule {

	private Tree tree;
	private TreeItem item;

	/**
	 * Subclasses should call super.appliesTo first, and then
	 * verify their conditions
	 * @param event
	 * @return
	 */
	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Tree && event.item instanceof TreeItem;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.tree = (Tree)event.widget;
		this.item = (TreeItem)event.item;
	}

	@Override
	protected String getWidgetAccessor() {
		StringBuilder res = new StringBuilder();
		res.append("bot.tree(");
		int index = WidgetUtils.getIndex(this.tree);
		if (index != 0) {
			res.append(index);
		}
		res.append(")");
		TreeItem current = this.item;
		res.append(".getTreeItem(\"");
		res.append(current.getText());
		res.append("\")");
		return res.toString();
	}
}
