package nl.spikey.orm.engine;

public class NotUniqueException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public NotUniqueException(String detailMessage)
	{
		super(detailMessage);
	}
}
