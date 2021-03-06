/*
 * This software is Copyright 2005,2006,2007,2008 Langdale Consultants.
 * Langdale Consultants can be contacted at: http://www.langdale.com.au
 */
/*
 * This software is Copyright 2005,2006,2007,2008 Langdale Consultants.
 * Langdale Consultants can be contacted at: http://www.langdale.com.au
 */
package au.com.langdale.sax;

import org.xml.sax.Attributes;

/**
 *	Represents an Element from an XML document.
 *
 * The Element's name, attributes and enclosing elements are
 * available through this interface.
 */
public interface XMLElement {
	public XMLElement getParent() ;
	public String getNameSpace();
	public String getName();
	public String getValue(String namespace, String name);
	public Attributes getAttributes();
	public boolean matches(String name);
	public boolean matches(String namespace, String name);
	public boolean matches( XMLElement parent, String name);
	public boolean matches( XMLElement parent, String namespace, String name);
}
