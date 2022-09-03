package io.github.xsmirnovx.oauth2.server.adapters.database.converter

import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import java.util.stream.Collectors
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class ClientAuthenticationMethodsConverter: AttributeConverter<Set<ClientAuthenticationMethod>, String> {

    override fun convertToDatabaseColumn(attribute: Set<ClientAuthenticationMethod>): String {
        return attribute.stream()
            .map { it.value }
            .collect(Collectors.joining(","))
    }

    override fun convertToEntityAttribute(dbData: String): Set<ClientAuthenticationMethod> {
        return dbData.split(",").stream()
            .map { ClientAuthenticationMethod(it) }
            .collect(Collectors.toSet())
    }
}
