/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.type;

import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.ObjectJavaTypeDescriptor;
import org.hibernate.type.descriptor.jdbc.JdbcTypeDescriptor;
import org.hibernate.type.descriptor.jdbc.ObjectNullResolvingJdbcTypeDescriptor;

/**
 * @author Christian Beikov
 */
public class NullType extends JavaObjectType {
	/**
	 * Singleton access
	 */
	public static final NullType INSTANCE = new NullType();

	public NullType() {
		super( ObjectNullResolvingJdbcTypeDescriptor.INSTANCE, ObjectJavaTypeDescriptor.INSTANCE );
	}

	public NullType(JdbcTypeDescriptor jdbcTypeDescriptor, JavaType<Object> javaTypeDescriptor) {
		super( jdbcTypeDescriptor, javaTypeDescriptor );
	}

	@Override
	public String getName() {
		return "null";
	}
}
