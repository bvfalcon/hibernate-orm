/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.boot.model.process.internal;

import java.util.function.Function;
import jakarta.persistence.TemporalType;

import org.hibernate.TimeZoneStorageStrategy;
import org.hibernate.annotations.TimeZoneStorageType;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.mapping.BasicValue;
import org.hibernate.metamodel.mapping.JdbcMapping;
import org.hibernate.metamodel.model.convert.spi.BasicValueConverter;
import org.hibernate.type.BasicType;
import org.hibernate.type.descriptor.java.BasicJavaType;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.MutabilityPlan;
import org.hibernate.type.descriptor.jdbc.JdbcTypeDescriptor;
import org.hibernate.type.descriptor.jdbc.JdbcTypeDescriptorIndicators;
import org.hibernate.type.spi.TypeConfiguration;

/**
 * @author Steve Ebersole
 */
@SuppressWarnings("rawtypes")
public class VersionResolution<E> implements BasicValue.Resolution<E> {

	// todo (6.0) : support explicit JTD?
	// todo (6.0) : support explicit STD?

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <E> VersionResolution<E> from(
			Function<TypeConfiguration, java.lang.reflect.Type> implicitJavaTypeAccess,
			Function<TypeConfiguration, BasicJavaType> explicitJtdAccess,
			Function<TypeConfiguration, JdbcTypeDescriptor> explicitStdAccess,
			TimeZoneStorageType timeZoneStorageType,
			TypeConfiguration typeConfiguration,
			@SuppressWarnings("unused") MetadataBuildingContext context) {

		// todo (6.0) : add support for Dialect-specific interpretation?

		final java.lang.reflect.Type implicitJavaType = implicitJavaTypeAccess.apply( typeConfiguration );
		final JavaType registered = typeConfiguration.getJavaTypeDescriptorRegistry().resolveDescriptor( implicitJavaType );
		final BasicJavaType jtd = (BasicJavaType) registered;

		final JdbcTypeDescriptor recommendedJdbcType = jtd.getRecommendedJdbcType(
				new JdbcTypeDescriptorIndicators() {
					@Override
					public TypeConfiguration getTypeConfiguration() {
						return typeConfiguration;
					}

					@Override
					public TemporalType getTemporalPrecision() {
						// if it is a temporal version, it needs to be a TIMESTAMP
						return TemporalType.TIMESTAMP;
					}

					@Override
					public TimeZoneStorageStrategy getDefaultTimeZoneStorageStrategy() {
						if ( timeZoneStorageType != null ) {
							switch ( timeZoneStorageType ) {
								case COLUMN:
									return TimeZoneStorageStrategy.COLUMN;
								case NATIVE:
									return TimeZoneStorageStrategy.NATIVE;
								case NORMALIZE:
									return TimeZoneStorageStrategy.NORMALIZE;
							}
						}
						return context.getBuildingOptions().getDefaultTimeZoneStorage();
					}
				}
		);

		final BasicType<?> basicType = typeConfiguration.getBasicTypeRegistry().resolve( jtd, recommendedJdbcType );
		final BasicType legacyType = typeConfiguration.getBasicTypeRegistry().getRegisteredType( jtd.getJavaType() );

		assert legacyType.getJdbcTypeDescriptor().equals( recommendedJdbcType );

		return new VersionResolution<>( jtd, recommendedJdbcType, basicType, legacyType );
	}

	private final JavaType jtd;
	private final JdbcTypeDescriptor jdbcTypeDescriptor;

	private final JdbcMapping jdbcMapping;
	private final BasicType legacyType;

	public VersionResolution(
			JavaType javaTypeDescriptor,
			JdbcTypeDescriptor jdbcTypeDescriptor,
			JdbcMapping jdbcMapping,
			BasicType legacyType) {
		this.jtd = javaTypeDescriptor;
		this.jdbcTypeDescriptor = jdbcTypeDescriptor;
		this.jdbcMapping = jdbcMapping;
		this.legacyType = legacyType;
	}

	@Override
	public JdbcMapping getJdbcMapping() {
		return jdbcMapping;
	}

	@Override
	@SuppressWarnings("unchecked")
	public BasicType getLegacyResolvedBasicType() {
		return legacyType;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JavaType<E> getDomainJavaDescriptor() {
		return jtd;
	}

	@Override
	public JavaType<?> getRelationalJavaDescriptor() {
		return jtd;
	}

	@Override
	public JdbcTypeDescriptor getJdbcTypeDescriptor() {
		return jdbcTypeDescriptor;
	}

	@Override
	public BasicValueConverter<E,E> getValueConverter() {
		return null;
	}

	@Override
	public MutabilityPlan<E> getMutabilityPlan() {
		return ImmutableMutabilityPlan.instance();
	}
}
