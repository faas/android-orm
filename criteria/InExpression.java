package nl.spikey.orm.criteria;

import java.util.Collection;

import nl.spikey.orm.IdObject;

class InExpression implements Criterion
{

	private String property;

	private Collection< ? > values;

	InExpression(String property, Collection< ? > values)
	{
		this.property = property;
		this.values = values;
	}

	@Override
	public String getExpression()
	{
		String expression = property + " IN (";
		for (Object value : values)
		{
			if (value instanceof IdObject)
				expression += "" + ((IdObject) value).getId();
			else
				expression += value.toString();
			expression += ',';
		}
		expression = expression.substring(0, expression.length() - 1) + ')';
		return expression;
	}

	@Override
	public String getArgument()
	{
		// In Expressions have no Arguments
		return null;
	}

}
