package io.github.xsmirnovx.oauth2.server.adapters.database.converter

import org.springframework.security.oauth2.core.AuthorizationGrantType
import java.util.stream.Collectors
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class AuthorizationGrantTypesConverter: AttributeConverter<Set<AuthorizationGrantType>, String> {

    override fun convertToDatabaseColumn(attribute: Set<AuthorizationGrantType>): String {
        return attribute.stream().map { it.value }.collect(Collectors.joining(","))
    }

    override fun convertToEntityAttribute(dbData: String): Set<AuthorizationGrantType> {
        return dbData.split(",")
            .stream()
            .map { AuthorizationGrantType(it) }
            .collect(Collectors.toSet())
    }
}
