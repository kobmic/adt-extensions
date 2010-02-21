/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.eclipse.adt.extensions.ui.wizards;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.jayway.eclipse.adt.xml.AndroidManifest;

/**
 * Facade for the manifest file manipulation. 
 * @author Michael Kober
 *
 */
public class AndroidManifestFile {
	
	private IResource resource = null;
	private static final String MANIFEST_FILE = "AndroidManifest.xml";
	
	/**
	 * Constructor.
	 * @param project current project
	 */
	public AndroidManifestFile(IProject project) {
		resource = project.findMember(MANIFEST_FILE);
	}
	
	/**
	 * Get the location of the android manifest file.
	 * @return manifest location as IPath
	 */
	private IPath getManifestLocation() {
		boolean resourceFound = ((resource != null) && ((resource.exists())) && (resource instanceof IFile));
		return resourceFound ? ((IFile) resource).getLocation() : null;
	}

	/**
	 * Update the Android manifest.
	 * @param activityName name of the new activity
	 * @param actions intent actions
	 * @param categories intent categories
	 * @throws CoreException 
	 */
	public void update(String activityName, Set<String> actions, Set<String> categories) throws CoreException {
		IPath path = getManifestLocation();
		if (path != null) {
			AndroidManifest manifest = new AndroidManifest(path.toOSString());
			manifest.addActivity(activityName, asArray(actions), asArray(categories));
			manifest.save();
			resource.refreshLocal(0, null);
		} else {
			Status status = new Status(IStatus.ERROR, "com.jayway.adt.extensions", "Could not find Android manifest file.");
			throw new CoreException(status);
		}
	}
	
	/**
	 * helper method.
	 * @param set set of intents or categories
	 * @return set as array
	 */
	private String[] asArray(Set<String> set) {
		List<String> list = new ArrayList<String>();
		for (String entry : set) {
			list.add(entry);
		}
		return list.toArray(new String[]{});
	}
}
