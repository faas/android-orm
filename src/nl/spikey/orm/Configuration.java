package nl.spikey.orm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Configuration
{
	private Map<String, Class< ? >> classes = new HashMap<String, Class< ? >>();

	private boolean foreignKeyEnabled = false;

	private Session session;

	public Configuration(Session session)
	{
		this.setSession(session);
	}

	public void addAnnotatedClass(Class< ? > clazz)
	{
		classes.put(clazz.getSimpleName(), clazz);
	}

	public Map<String, Class< ? >> getClassMap()
	{
		return classes;
	}

	public Set<String> getClassNames()
	{
		return classes.keySet();
	}

	public Collection<Class< ? >> getClasses()
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
