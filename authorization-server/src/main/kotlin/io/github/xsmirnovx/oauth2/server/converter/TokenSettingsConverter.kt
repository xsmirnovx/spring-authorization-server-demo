package io.github.xsmirnovx.oauth2.server.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class TokenSettingsConverter(
    private val objectMapper: ObjectMapper
) : AttributeConverter<TokenSettings, String> {

//    @Autowired
//    private val objectMapper: ObjectMapper

    override fun convertToDatabaseColumn(attribute: TokenSettings): String {
        return try {
            objectMapper!!.writeValueAsString(attribute.settings)
        } catch (ex: Exception) {
            ""
        }
    }

    override fun convertToEntityAttribute(dbData: String): TokenSettings {
        val map: Map<String, Any>
        return try {
            map = objectMapper.readValue(
                dbData,
                object : TypeReference<Map<String, Any>>() {})
            TokenSettings.withSettings(map).build()

        } catch (ex: Exception) {
            throw RuntimeException()
        }
    }
}