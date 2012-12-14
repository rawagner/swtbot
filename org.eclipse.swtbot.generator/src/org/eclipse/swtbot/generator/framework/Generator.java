package org.eclipse.swtbot.generator.framework;

import java.util.List;

public interface Generator {

	public List<GenerationRule> createRules();
	public String getLabel();

}
