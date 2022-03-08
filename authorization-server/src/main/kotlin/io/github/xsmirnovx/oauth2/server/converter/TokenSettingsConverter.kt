package io.github.xsmirnovx.oauth2.server.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class TokenSettingsConverter(private val objectMapper: ObjectMapper) :
        AttributeConverter<TokenSettings, String> {

    override fun convertToDatabaseColumn(attribute: TokenSettings): String {

        return kotlin.runCatching {
            objectMapper.writeValueAsString(attribute.settings)
        }
        .getOrThrow()
    }

    override fun convertToEntityAttribute(dbData: String): TokenSettings {

        return kotlin.runCatching {
            val map = objectMapper.readValue(dbData, object : TypeReference<Map<String, Any>>() {})
            TokenSettings.withSettings(map).build()
        }
        .getOrThrow()
    }
}