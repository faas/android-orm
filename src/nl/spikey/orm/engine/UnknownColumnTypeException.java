package nl.spikey.orm.engine;

public class UnknownColumnTypeException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public UnknownColumnTypeException(String detailMessage)
	{
		super(detailMessage);
	}
}
