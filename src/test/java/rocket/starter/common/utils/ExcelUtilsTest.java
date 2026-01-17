package rocket.starter.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * @author ron 2022/10/24
 */
public class ExcelUtilsTest {

  @Test
  public void testBuildResponse() {
    byte[] bytes = {0};
    ResponseEntity<Resource> resourceResponseEntity =
        ExcelUtils.buildResponse(new ByteArrayResource(bytes));

    Assertions.assertEquals(MediaType.parseMediaType("application/octet-stream"),
        resourceResponseEntity.getHeaders().getContentType());

    ByteArrayResource body = (ByteArrayResource) resourceResponseEntity.getBody();
    Assertions.assertNotNull(body);

    Assertions.assertEquals(bytes, body.getByteArray());
  }

  @Test
  public void testGetExcelResource() throws IOException {
    Resource excelResource = ExcelUtils.getExcelResource(new String[] {"A", "B"},
        Collections.singletonList(new String[] {"value1", "value2"}));


    String result = IOUtils.toString(excelResource.getInputStream(), StandardCharsets.UTF_8);
    Assertions.assertEquals("A,B\n\"value1\",\"value2\"", result);
  }

  @Test
  public void testBuildResponse2() throws IOException {
    ResponseEntity<StreamingResponseBody> response =
        ExcelUtils.buildResponse(new String[] {"A", "B"}, lastId -> {
          if (lastId == null) {
            return new PageImpl<>(
                Collections.singletonList(new String[] {"1", "value1", "value2"}));
          }
          return new PageImpl<>(Collections.emptyList());
        });

    StreamingResponseBody body = response.getBody();
    Assertions.assertNotNull(body);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    response.getBody().writeTo(bos);

    String result = bos.toString(StandardCharsets.UTF_8);
    Assertions.assertEquals("A,B\n\"value1\",\"value2\"", result);
  }


}
