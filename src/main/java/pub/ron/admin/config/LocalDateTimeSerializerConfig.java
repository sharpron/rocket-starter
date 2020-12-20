package pub.ron.admin.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalDateTimeSerializerConfig {


  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> builder
        .serializerByType(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
          @Override
          public void serialize(LocalDateTime o, JsonGenerator jsonGenerator,
              SerializerProvider serializerProvider) throws IOException {
            if (o != null) {
              final long epochMilli = o.toInstant(ZoneOffset.of("+8")).toEpochMilli();
              jsonGenerator.writeNumber(epochMilli);
            }
          }
        });
  }
}
