package io.github.xsmirnovx.oauth2.server.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class ClientSettingsConverter(
    private val objectMapper: ObjectMapper
) : AttributeConverter<ClientSettings, String> {

    override fun convertToDatabaseColumn(attribute: ClientSettings): String {
        return try {
            objectMapper.writeValueAsString(attribute.settings)
        } catch (ex: Exception) {
            ""
        }
    }

    override fun convertToEntityAttribute(dbData: String): ClientSettings {
        val map: Map<String, Any>
        return try {
            map = objectMapper.readValue(
                dbData,
                object : TypeReference<Map<String, Any>>() {})
            ClientSettings.withSettings(map).build()

        } catch (ex: Exception) {
            throw RuntimeException()
        }
    }
}