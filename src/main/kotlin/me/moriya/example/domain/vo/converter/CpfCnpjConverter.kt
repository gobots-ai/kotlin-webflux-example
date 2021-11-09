package me.moriya.example.domain.vo.converter

import me.yanaga.opes.CpfCnpj
import org.apache.commons.lang3.StringUtils
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import java.util.*

class CpfCnpjConverter {

    @WritingConverter
    class CpfCnpjWritingConverter : Converter<CpfCnpj, String> {
        override fun convert(source: CpfCnpj): String? {
            if(Objects.nonNull(source))
                return source.toString()
            return null
        }
    }

    @ReadingConverter
    class CpfCnpjReadingConverter : Converter<String, CpfCnpj> {
        override fun convert(source: String): CpfCnpj? {
            return if(StringUtils.isNotBlank(source))
                CpfCnpj.of(source)
            else null
        }
    }

}