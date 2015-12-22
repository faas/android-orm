package nl.spikey.orm.criteria;

import java.util.ArrayList;
import java.util.Arrays;
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

	public void addIsNull(String property, Boolean isNull)
	{
		if (isNull != null)
		{
			if (isNull.booleanValue())
			{
				criterion.add(Restrictions.isNull(property));
			}
			else
			{
				criterion.add(Restrictions.isNotNull(property));
			}
		}
	}

	public void addOrEquals(String property1, String property2, Object value)
	{
		if (value != null)
		{
			criterion.add(Restrictions.or(Restrictions.eq(property1, value),
				Restrictions.eq(property2, value)));
		}
	}

	public void addOrs(Criterion... list)
	{
		addOrs(Arrays.asList(list));
	}

	public void addOrs(List<Criterion> list)
	{
		if (!list.isEmpty())
		{
			Junction ors = Restrictions.disjunction();
			for (Criterion curCrit : list)
			{
				ors.add(curCrit);
			}
			criterion.add(ors);
		}
	}

	@Override
	public String toString()
	{
		StringBuilder criteaString = new StringBuilder();
		for(Criterion crit : criterion){
			criteaString.append(", ");
			criteaString.append(crit.toString());
		}
		return criteaString.toString();
	}
}
