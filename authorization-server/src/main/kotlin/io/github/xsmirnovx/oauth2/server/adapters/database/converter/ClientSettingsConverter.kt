package io.github.xsmirnovx.oauth2.server.adapters.database.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class ClientSettingsConverter(private val objectMapper: ObjectMapper) :
        AttributeConverter<ClientSettings, String> {

    override fun convertToDatabaseColumn(attribute: ClientSettings): String {

        return kotlin.runCatching {
            objectMapper.writeValueAsString(attribute.settings)
        }
        .getOrThrow()
    }

    override fun convertToEntityAttribute(dbData: String): ClientSettings {

        return kotlin.runCatching {
            val map = objectMapper.readValue(dbData, object : TypeReference<Map<String, Any>>() {})
            ClientSettings.withSettings(map).build()
        }
        .getOrThrow()
    }
}