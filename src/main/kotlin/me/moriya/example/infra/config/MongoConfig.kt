package me.moriya.example.infra.config

import com.google.common.collect.Lists
import me.moriya.example.domain.vo.converter.CpfCnpjConverter
import me.moriya.example.domain.vo.converter.NameConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
class MongoConfig {

    @Bean
    fun customConversions(): MongoCustomConversions {
        val converters: MutableList<Converter<*, *>> = Lists.newArrayList()
        converters.add(NameConverter.NameWritingConverter())
        converters.add(NameConverter.NameReadingConverter())
        converters.add(CpfCnpjConverter.CpfCnpjWritingConverter())
        converters.add(CpfCnpjConverter.CpfCnpjReadingConverter())
        return MongoCustomConversions(converters)
    }


}