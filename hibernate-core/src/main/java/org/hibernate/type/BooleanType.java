/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.type;

import java.sql.Types;

import org.hibernate.Incubating;
import org.hibernate.type.descriptor.java.BooleanJavaTypeDescriptor;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.BooleanJdbcTypeDescriptor;
import org.hibernate.type.descriptor.jdbc.JdbcTypeDescriptor;

/**
 * A type that maps between {@link Types#BOOLEAN BOOLEAN} and {@link Boolean}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class BooleanType
		extends AbstractSingleColumnStandardBasicType<Boolean>
		implements AdjustableBasicType<Boolean> {
	public static final BooleanType INSTANCE = new BooleanType();

	public BooleanType() {
		this( BooleanJdbcTypeDescriptor.INSTANCE, BooleanJavaTypeDescriptor.INSTANCE );
	}

	protected BooleanType(JdbcTypeDescriptor jdbcTypeDescriptor, BooleanJavaTypeDescriptor javaTypeDescriptor) {
		super( jdbcTypeDescriptor, javaTypeDescriptor );
	}

	@Incubating
	public BooleanType(JdbcTypeDescriptor jdbcTypeDescriptor, JavaType<Boolean> javaTypeDescriptor) {
		super( jdbcTypeDescriptor, javaTypeDescriptor );
	}

	@Override
	public String getName() {
		return "boolean";
	}

	@Override
	public String[] getRegistrationKeys() {
		return new String[] { getName(), boolean.class.getName(), Boolean.class.getName() };
	}

}
