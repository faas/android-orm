package nl.spikey.orm.criteria;

import java.util.Collection;

import nl.spikey.orm.criteria.Junction.Nature;

public class Restrictions
{

	Restrictions()
	{
		// cannot be instantiated
	}

	/**
	 * Apply an "equal" constraint to the named property
	 * 
	 * @param property
	 * @param value
	 * @return Criterion
	 */
	public static Criterion eq(String property, Object value)
	{
		return new SimpleExpression(property, value, "=");
	}

	/**
	 * Apply a "not equal" constraint to the named property
	 * 
	 * @param property
	 * @param value
	 * @return Criterion
	 */
	public static Criterion ne(String property, Object value)
	{
		return new SimpleExpression(property, value, "<>");
	}

	/**
	 * Apply a "greater than" constraint to the named property
	 * 
	 * @param property
	 * @param value
	 * @return Criterion
	 */
	public static Criterion gt(String property, Object value)
	{
		return new SimpleExpression(property, value, ">");
	}

	/**
	 * Apply a "less than" constraint to the named property
	 * 
	 * @param property
	 * @param value
	 * @return Criterion
	 */
	public static Criterion lt(String property, Object value)
	{
		return new SimpleExpression(property, value, "<");
	}

	/**
	 * Apply a "less than or equal" constraint to the named property
	 * 
	 * @param property
	 * @param value
	 * @return Criterion
	 */
	public static Criterion le(String property, Object value)
	{
		return new SimpleExpression(property, value, "<=");
	}

	/**
	 * Apply a "greater than or equal" constraint to the named property
	 * 
	 * @param property
	 * @param value
	 * @return Criterion
	 */
	public static Criterion ge(String property, Object value)
	{
		return new SimpleExpression(property, value, ">=");
	}

	/**
	 * Apply a "like" constraint to the named property
	 * 
	 * @param property
	 * @param value
	 * @return Criterion
	 */
	public static Criterion like(String property, Object value)
	{
		return new SimpleExpression(property, value, " like ");
	}

	/**
	 * Apply a "like" constraint to the named property
	 * 
	 * @param property
	 * @param value
	 * @return Criterion
	 */
	public static Criterion like(String property, String value, MatchMode matchMode)
	{
		return new SimpleExpression(property, matchMode.stringToMatch(value), " like ");
	}

	/**
	 * Apply an "in" constraint to the named property
	 * 
	 * @param property
	 * @param values
	 * @return Criterion
	 */
	public static Criterion in(String property, Collection< ? > values)
	{
		return new InExpression(property, values);
	}

	/**
	 * Apply an "not in" constraint to the named property
	 * 
	 * @param property
	 * @param values
	 * @return Criterion
	 */
	public static Criterion notIn(String property, Collection< ? > values)
	{
		return new NotInExpression(property, values);
	}

	/**
	 * Apply an "not in" constraint to the named property
	 * 
	 * @param property
	 * @param values
	 * @return Criterion
	 */
	public static Criterion notInType2(String property, Collection< ? > values)
	{
		return new NotExpression(new NotInExpression(property, values));
	}

	/**
	 * Return the negation of an expression
	 * 
	 * @param criterion
	 * @return Criterion
	 */
	public static Criterion Not(Criterion criterion)
	{
		return new NotExpression(criterion);
	}

	/**
	 * Apply an "is null" constraint to the named property
	 * 
	 * @return Criterion
	 */
	public static Criterion isNull(String property)
	{
		return new NullExpression(property);
	}

	/**
	 * Apply an "is not null" constraint to the named property
	 * 
	 * @return Criterion
	 */
	public static Criterion isNotNull(String property)
	{
		return new NotNullExpression(property);
	}

	/**
	 * Return the disjuction of two expressions
	 * 
	 * @return Criterion
	 */
	public static LogicalExpression or(Criterion criterion1, Criterion criterion2)
	{
		return new LogicalExpression(criterion1, criterion2, "OR");
	}

	public static Junction disjunction()
	{
		return new Junction(Nature.OR);
	}
}