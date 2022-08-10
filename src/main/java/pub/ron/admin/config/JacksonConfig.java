package pub.ron.admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  /**
   * 自定义ObjectMapper.
   * 添加时间处理.
   *
   * @return ObjectMapper
   */
  @Bean
  public ObjectMapper getObjectMapper() {
    return JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .build();
  }
}
