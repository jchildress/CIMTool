/*
 * This software is Copyright 2005,2006,2007,2008 Langdale Consultants.
 * Langdale Consultants can be contacted at: http://www.langdale.com.au
 */
package au.com.langdale.cimtoole.wizards;

import static au.com.langdale.ui.builder.Templates.CheckBox;
import static au.com.langdale.ui.builder.Templates.CheckboxTableViewer;
import static au.com.langdale.ui.builder.Templates.FileField;
import static au.com.langdale.ui.builder.Templates.Grid;
import static au.com.langdale.ui.builder.Templates.Group;
import static au.com.langdale.ui.builder.Templates.Label;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;

import au.com.langdale.cimtoole.project.Info;
import au.com.langdale.ui.builder.FurnishedWizardPage;
import au.com.langdale.ui.builder.Template;
import au.com.langdale.workspace.ResourceUI.ProfileBinding;
import au.com.langdale.workspace.ResourceUI.ProjectBinding;

public class RuleWizardPage extends FurnishedWizardPage {
	

	private String pathname;
	private boolean copyDefault;
	private boolean importing;
	private String[] sources;
	private String type;
	
	private ProjectBinding projects = new ProjectBinding();
	private ProfileBinding profiles = new ProfileBinding();

	public RuleWizardPage() {
		super("main");
	}

	public void setSelected(IStructuredSelection selection) {
		projects.setSelected(selection);
		profiles.setSelected(selection);
	}

	public String getPathname() {
		return pathname;
	}

	public boolean isCopyDefault() {
		return copyDefault;
	}
	
	public IFile getFile() {
		return Info.getRelated(profiles.getFile(), type);
	}

	public String[] getSources() {
		return sources;
	}

	public void setSources(String[] extensions) {
		sources = extensions;
		importing = extensions != null;
	}

	public String getType() {
		return type;
	}

	public void setType(String extension) {
		type = extension;
	}

	@Override
	protected Content createContent() {
		return new Content() {

			@Override
			protected Template define() {
				if( importing )
					return Grid(
						Group(FileField("source", "File to import:", sources)),
						Group(Label("Project"), Label("Profile")),
						Group(CheckboxTableViewer("projects"), CheckboxTableViewer("profiles"))
					);
				else
					return Grid(
						Group(Label("Project"), Label("Profile")),
						Group(CheckboxTableViewer("projects"), CheckboxTableViewer("profiles")),
						Group(CheckBox("copy", "Copy the standard rules"))
					);
			}

			@Override
			protected void addBindings() {
				projects.bind("projects", this);
				profiles.bind("profiles", this, projects);
				if( ! importing)
					getButton("copy").setSelection(true);
			}

			@Override
			public String validate() {
				
				// the source file
				if(importing) {
					// TODO: replace with TextBinding.
					pathname = getText("source").getText().trim();
					if( pathname.length() == 0)
						return "A file to import must be chosen";
					File source = new File(pathname);
					if( ! source.canRead())
						return "The chosen file cannot be read";
				}
				else {
					copyDefault = getButton("copy").getSelection();
				}
			
				if( getFile().exists() )
					return "Rules already exist for the selected profile.";
				
				return null;
			}
		};
	}
}