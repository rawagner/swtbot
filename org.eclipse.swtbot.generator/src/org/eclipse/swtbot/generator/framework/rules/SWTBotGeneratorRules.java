package org.eclipse.swtbot.generator.framework.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.Generator;

public class SWTBotGeneratorRules implements Generator {

	public List<GenerationRule> createRules() {
		List<GenerationRule> res = new ArrayList<GenerationRule>();
		res.add(new ButtonClickedRule());
		res.add(new ExpandTreeItemRule());
		res.add(new DoubleClickTreeItemRule());
		res.add(new MenuClickedRule());
		res.add(new SelectTreeItemRule());
		return res;
	}

	public String getLabel() {
		return "SWTBot";
	}
}
