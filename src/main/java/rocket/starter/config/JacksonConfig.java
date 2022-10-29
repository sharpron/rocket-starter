package rocket.starter.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jackson 配置.
 *
 * @author ron
 */
@Configuration
public class JacksonConfig {

  private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd");
  private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm");


  /**
   * 配置默认日期时间序列化.
   *
   * @return Jackson2ObjectMapperBuilderCustomizer
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> builder
        .serializerByType(LocalDate.class, new JsonSerializer<LocalDate>() {
          @Override
          public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
              throws IOException {
            if (value != null) {
              gen.writeString(DEFAULT_DATE_FORMATTER.format(value));
            }
          }
        })
        .deserializerByType(LocalDate.class, new JsonDeserializer<>() {
          @Override
          public LocalDate deserialize(JsonParser p, DeserializationContext context)
              throws IOException {
            String valueAsString = p.getValueAsString();
            return LocalDate.parse(valueAsString);
          }
        })
        .serializerByType(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
          @Override
          public void serialize(LocalDateTime value, JsonGenerator gen,
                                SerializerProvider serializerProvider) throws IOException {
            if (value != null) {
              gen.writeString(DEFAULT_DATE_TIME_FORMATTER.format(value));
            }
          }
        })
        .deserializerByType(LocalDateTime.class, new JsonDeserializer<>() {
          @Override
          public LocalDateTime deserialize(JsonParser p, DeserializationContext context)
              throws IOException {
            String valueAsString = p.getValueAsString();
            return LocalDateTime.parse(valueAsString);
          }
        });
  }

}
