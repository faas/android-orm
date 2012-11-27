package nl.spikey.orm.criteria;

import java.util.Arrays;
import java.util.List;

import nl.spikey.orm.IdObject;

class SimpleExpression implements Criterion
{

	private String property;

	private String value;

	private String operator;

	SimpleExpression(String property, Object value, String operator)
	{
		this.property = property;
		if (value instanceof IdObject)
			this.value = "" + ((IdObject) value).getId();
		else
			this.value = value.toString();
		this.operator = operator;
	}

	@Override
	public String getExpression()
	{
		return property + operator + '?';
	}

	@Override
	public List<String> getArguments()
	{
		return Arrays.asList(value);
	}
}
