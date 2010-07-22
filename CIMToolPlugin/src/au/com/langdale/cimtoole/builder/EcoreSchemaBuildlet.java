package au.com.langdale.cimtoole.builder;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import au.com.langdale.cimtoole.CIMToolPlugin;
import au.com.langdale.cimtoole.project.EcoreTask;
import au.com.langdale.cimtoole.project.Info;
import au.com.langdale.kena.OntModel;

public class EcoreSchemaBuildlet extends Buildlet {
	@Override
	protected void build(IFile result, IProgressMonitor monitor) throws CoreException {
		IProject project = result.getProject();
		IFolder folder = Info.getSchemaFolder(project);
		OntModel schema = CIMToolPlugin.getCache().getMergedOntologyWait(folder);
		String namespace = Info.getProperty(project, Info.SCHEMA_NAMESPACE);
		try{
			
			EPackage ecoreModel = new EcoreTask(schema).createEcore(true, "cim", namespace);
			URI fileURI = URI.createFileURI(result.getLocation().toFile().getAbsolutePath());
			Resource ecore = new ResourceSetImpl().createResource(fileURI);
			ecore.getContents().add(ecoreModel);
			try {
				ecore.save(Collections.EMPTY_MAP);
			} catch (IOException e) {
				Info.error("can't write to " +result.getLocation().toString());
			}
			result.refreshLocal(0, new NullProgressMonitor());
		}catch (Exception e){
			e.printStackTrace();
			throw new CoreException(new Status(IStatus.ERROR, CIMToolPlugin.PLUGIN_ID, "Error creating filled profile", e));
		}

	}
	
	@Override
	protected Collection getOutputs(IResource file) throws CoreException {
		if( Info.isSchema(file)) {
			IProject project = file.getProject();
			String name = Info.getProperty(project, Info.MERGED_SCHEMA_PATH);
			if( name != null && name.length() != 0)
				return Collections.singletonList(project.getFile(name)); 
		}
		return Collections.EMPTY_LIST;
	}

}