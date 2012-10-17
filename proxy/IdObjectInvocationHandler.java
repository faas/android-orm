package nl.spikey.orm.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import nl.spikey.orm.IdObject;

public class IdObjectInvocationHandler<T extends IdObject> implements InvocationHandler
{
	private T object;

	public IdObjectInvocationHandler(T object)
	{
		this.object = object;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		String name = method.getName();
		if (Object.class == method.getDeclaringClass())
		{
			if ("equals".equals(name))
			{
				return proxy == args[0];
			}
			else if ("hashCode".equals(name))
			{
				return System.identityHashCode(proxy);
			}
			else if ("toString".equals(name))
			{
				return proxy.getClass().getName() + "@"
					+ Integer.toHexString(System.identityHashCode(proxy))
					+ ", with InvocationHandler " + this;
			}
			else
			{
				throw new IllegalStateException(String.valueOf(method));
			}
		}
		// TODO: haal volledige object binnen.
		return method.invoke(object, args);
	}
}
