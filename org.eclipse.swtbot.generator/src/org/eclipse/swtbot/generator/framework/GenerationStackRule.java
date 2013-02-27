package org.eclipse.swtbot.generator.framework;

import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher.CodeGenerationListener;

public abstract class GenerationStackRule {
	
	public abstract List<GenerationRule> getGenerationRules();
	public abstract String generate(GenerationRule rule);

	public String generateCode() {
		return getAccessor() + getActon();
	}

	protected abstract String getAccessor();
	protected abstract String getActon();
	protected abstract void setCodeGenerationListener(CodeGenerationListener generationListener);

}
