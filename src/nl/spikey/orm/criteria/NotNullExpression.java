package nl.spikey.orm.criteria;

import java.util.List;

class NotNullExpression implements Criterion
{
	private String property;

	NotNullExpression(String property)
	{
		this.property = property;
	}

	@Override
	public String getExpression()
	{
		return property + " IS NOT NULL";
	}

	@Override
	public List<String> getArguments()
	{
		return null;
	}
}
