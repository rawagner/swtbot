package org.eclipse.swtbot.generator.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.generator.framework.Generator;

public class GeneratorExtensionPointManager {

	private final static String EXTENSION_POINT_ID = "org.eclipse.swtbot.generator.botGeneratorSupport";

	public static List<Generator> loadGenerators() {
		List<Generator> res = new ArrayList<Generator>();
		for (IConfigurationElement ext : Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID)) {
			try {
				Generator generator = (Generator)ext.createExecutableExtension("class");
				res.add(generator);
			} catch (CoreException ex) {
				// TODO log
			}
		}
		return res;
	}

}
