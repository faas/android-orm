package nl.spikey.orm.criteria;

/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Junction implements Criterion
{
	private final Nature nature;

	private final List<Criterion> criterions = new ArrayList<Criterion>();

	protected Junction(Nature nature)
	{
		this.nature = nature;
	}

	public Junction add(Criterion criterion)
	{
		criterions.add(criterion);
		return this;
	}

	public Nature getNature()
	{
		return nature;
	}

	public Iterable<Criterion> conditions()
	{
		return criterions;
	}

	@Override
	public String getExpression()
	{
		if (criterions.size() == 0)
		{
			return "";
		}

		StringBuilder buffer = new StringBuilder().append('(');
		Iterator<Criterion> itr = criterions.iterator();
		while (itr.hasNext())
		{
			buffer.append(itr.next().getExpression());
			if (itr.hasNext())
			{
				buffer.append(' ').append(nature.getOperator()).append(' ');
			}
		}
		return buffer.append(')').toString();
	}

	public static enum Nature
	{
		AND,
		OR;

		public String getOperator()
		{
			return name().toLowerCase();
		}
	}

	@Override
	public List<String> getArguments()
	{
		List<String> arguments = new ArrayList<String>();
		for (Criterion criterion : criterions)
			if (criterion.getArguments() != null)
				arguments.addAll(criterion.getArguments());

		return arguments;
	}
}
