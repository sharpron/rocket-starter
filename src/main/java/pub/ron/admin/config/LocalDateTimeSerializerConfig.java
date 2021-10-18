package pub.ron.admin.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalDateTimeSerializerConfig {

  /**
   * 设置默认时区为东八区.
   */
  static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+8");

  /**
   * jackson LocalDateTime serialize support.
   *
   * @return Jackson2ObjectMapperBuilderCustomizer
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder ->
        builder.serializerByType(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
              @Override
              public void serialize(
                  LocalDateTime o,
                  JsonGenerator jsonGenerator,
                  SerializerProvider serializerProvider)
                  throws IOException {
                if (o != null) {
                  final long epochMilli = o.toInstant(ZONE_OFFSET).toEpochMilli();
                  jsonGenerator.writeNumber(epochMilli);
                }
              }
            })
            .deserializerByType(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
              @Override
              public LocalDateTime deserialize(JsonParser jsonParser,
                                        DeserializationContext deserializationContext)
                  throws IOException {
                Long number = jsonParser.readValueAs(Long.class);
                if (number == null) {
                  return null;
                }
                return new Date(number).toInstant().atOffset(ZONE_OFFSET).toLocalDateTime();
              }
            });

  }
}
