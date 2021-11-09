package me.moriya.example.domain.vo.converter

import me.moriya.example.domain.vo.Name
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter

class NameConverter {

    @WritingConverter
    class NameWritingConverter : Converter<Name, String> {

        override fun convert(source: Name): String {
            return source.toString()
        }

    }

    @ReadingConverter
    class NameReadingConverter : Converter<String, Name> {

        override fun convert(source: String): Name? {
            return Name.of(source)
        }

    }

}
