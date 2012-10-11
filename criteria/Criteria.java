package nl.spikey.orm.criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.spikey.orm.IdObject;

public class Criteria
{
	private List<Criterion> criterion = new ArrayList<Criterion>();

	private String orderBy = null;

	private Long limitCount = Long.valueOf(1000);

	private Class< ? extends IdObject> clzz;

	public Criteria(Class< ? extends IdObject> clzz)
	{
		this.clzz = clzz;
	}

	/**
	 * vervangt het '*' door '%'
	 * 
	 * @param value
	 * @return value
	 */
	public String replaceRegEx(String value)
	{
		if (value != null)
			return value.replace('*', '%');
		return null;
	}

	public List<Criterion> getCriterion()
	{
		return criterion;
	}

	public void setCriterion(List<Criterion> criterion)
	{
		this.criterion = criterion;
	}

	public String getOrderBy()
	{
		return orderBy;
	}

	public void setOrderBy(String orderBy)
	{
		this.orderBy = orderBy;
	}

	public Long getLimitCount()
	{
		return limitCount;
	}

	public Class< ? extends IdObject> getClzz()
	{
		return clzz;
	}

	public void setClzz(Class< ? extends IdObject> clzz)
	{
		this.clzz = clzz;
	}

	public String getLimitString()
	{
		if (limitCount != null)
			return limitCount.toString();
		return null;
	}

	public void setLimitCount(Long limitCount)
	{
		this.limitCount = limitCount;
	}

	public void addEquals(String property, Object value)
	{
		if (value != null)
			criterion.add(Restrictions.eq(property, value));
	}

	public void addGreaterThan(String property, Object value)
	{
		if (value != null)
			criterion.add(Restrictions.gt(property, value));
	}

	public void addGreaterOrEquals(String property, Object value)
	{
		if (value != null)
			criterion.add(Restrictions.ge(property, value));
	}

	public void addLessThan(String property, Object value)
	{
		if (value != null)
			criterion.add(Restrictions.lt(property, value));
	}

	public void addLessOrEquals(String property, Object value)
	{
		if (value != null)
			criterion.add(Restrictions.le(property, value));
	}

	public void addLike(String property, String value, MatchMode mode)
	{
		if (value != null)
			criterion.add(Restrictions.like(property, replaceRegEx(value), mode));
	}

	public void addIn(String property, Collection< ? > values)
	{
		if (values != null && !values.isEmpty())
			criterion.add(Restrictions.in(property, values));
	}

	public void addNotIn(String property, Collection< ? > values)
	{
		if (values != null && !values.isEmpty())
			criterion.add(Restrictions.notIn(property, values));
	}

	public void addIsNull(String expression, Boolean isNull)
	{
		if (isNull != null)
		{
			if (isNull.booleanValue())
			{
				criterion.add(Restrictions.isNull(expression));
			}
			else
			{
				criterion.add(Restrictions.isNotNull(expression));
			}
		}
	}
}
