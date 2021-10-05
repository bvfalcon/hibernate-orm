/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.type;

import org.hibernate.type.descriptor.java.PrimitiveCharacterArrayJavaTypeDescriptor;
import org.hibernate.type.descriptor.jdbc.NClobJdbcTypeDescriptor;

/**
 * Map a char[] to a NClob
 *
 * @author Emmanuel Bernard
 */
public class PrimitiveCharacterArrayNClobType extends AbstractSingleColumnStandardBasicType<char[]> {
	public static final CharacterArrayNClobType INSTANCE = new CharacterArrayNClobType();

	public PrimitiveCharacterArrayNClobType() {
		super( NClobJdbcTypeDescriptor.DEFAULT, PrimitiveCharacterArrayJavaTypeDescriptor.INSTANCE );
	}

	public String getName() {
		// todo name these annotation types for addition to the registry
		return null;
	}
}
