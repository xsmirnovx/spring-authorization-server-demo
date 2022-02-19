package io.github.xsmirnovx.oauth2.server.converter

import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class StringListToStringConverter : AttributeConverter<Set<String>, String> {

    // TODO: refactor without optional
    override fun convertToDatabaseColumn(attribute: Set<String>): String {
        return Optional.ofNullable(attribute)
            .filter { attr: Set<String> -> attr.isNotEmpty() }
            .map { java.lang.String.join(",", attribute) }
            .orElse("")
    }

    override fun convertToEntityAttribute(dbData: String): Set<String> {
        return Optional.ofNullable(dbData)
            .filter { data: String -> data.trim { it <= ' ' }.isNotEmpty() }
            .map { data: String -> setOf(*data.split(",").toTypedArray()) }
            .orElseGet { setOf() }
    }
}