/*
 * Copyright (c) 2008, 2009 Sun Microsystems. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Linda DeMichiel - Java Persistence 2.0 - Version 2.0 (October 1, 2009)
 *     Specification available from http://jcp.org/en/jsr/detail?id=317
 */

// $Id: Column.java 20957 2011-06-13 09:58:51Z stliu $
package nl.spikey.orm.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Is used to specify the mapped column for a persistent property or field. If no
 * <code>Column</code> annotation is specified, the default values apply.
 * 
 * <blockquote>
 * 
 * <pre>
 *    Example 1:
 * 
 *    &#064;Column(name="DESC", nullable=false, length=512)
 *    public String getDescription() { return description; }
 * 
 *    Example 2:
 * 
 *    &#064;Column(name="DESC",
 *            columnDefinition="CLOB NOT NULL",
 *            table="EMP_DETAIL")
 *    &#064;Lob
 *    public String getDescription() { return description; }
 * 
 *    Example 3:
 * 
 *    &#064;Column(name="ORDER_COST", updatable=false, precision=12, scale=2)
 *    public BigDecimal getCost() { return cost; }
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * 
 * @since Java Persistence 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Column
{

	/**
	 * (Optional) The name of the column. Defaults to the property or field name.
	 */
	String name() default "";

	/**
	 * (Optional) Whether the column is a unique key. This is a shortcut for the
	 * <code>UniqueConstraint</code> annotation at the table level and is useful for when
	 * the unique key constraint corresponds to only a single column. This constraint
	 * applies in addition to any constraint entailed by primary key mapping and to
	 * constraints specified at the table level.
	 */
	boolean unique() default false;

	/**
	 * (Optional) Whether the database column is nullable.
	 */
	boolean nullable() default true;

	/**
	 * (Optional) The column length. (Applies only if a string-valued column is used.)
	 */
	int length() default 255;
}
