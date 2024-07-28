package org.bts.backend.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.bts.backend.domain.constant.AuthProvider;

@Converter
public class AuthProviderConverter implements AttributeConverter<AuthProvider, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AuthProvider authProvider) {
        return authProvider.getCode();
    }

    @Override
    public AuthProvider convertToEntityAttribute(Integer code) {
        return AuthProvider.of(code);
    }
}
