package nl.spikey.orm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Configuration
{
	private Map<String, Class< ? extends IdObject>> classes =
		new HashMap<String, Class< ? extends IdObject>>();

	private boolean foreignKeyEnabled = false;

	/**
	 * The session is used to call de database methodes
	 */
	private Session session;

	public Configuration(Session session)
	{
		this.setSession(session);
	}

	public void addAnnotatedClass(Class< ? extends IdObject> clazz)
	{
		classes.put(clazz.getSimpleName(), clazz);
	}

	public Map<String, Class< ? extends IdObject>> getClassMap()
	{
		return classes;
	}

	public Set<String> getClassNames()
	{
		return classes.keySet();
	}

	public Collection<Class< ? extends IdObject>> getClasses()
	{
		return classes.values();
	}

	public boolean isForeignKeyEnabled()
	{
		return foreignKeyEnabled;
	}

	public void setForeignKeyEnabled(boolean foreignKeyEnabled)
	{
		this.foreignKeyEnabled = foreignKeyEnabled;
	}

	public Session getSession()
	{
		return session;
	}

	public void setSession(Session session)
	{
		this.session = session;
	}
}
