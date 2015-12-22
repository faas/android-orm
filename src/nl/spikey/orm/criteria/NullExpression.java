package nl.spikey.orm.criteria;

import java.util.List;

class NullExpression implements Criterion
{
	private String property;

	NullExpression(String property)
	{
		this.property = property;
	}

	@Override
	public String getExpression()
	{
		return property + " IS NULL";
	}

	@Override
	public List<String> getArguments()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return getExpression();
	}
}
