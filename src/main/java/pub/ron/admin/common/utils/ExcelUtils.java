package pub.ron.admin.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * excel 常用工具.
 *
 * @author ron 2022/8/6
 */
public class ExcelUtils {

  private ExcelUtils() {

  }

  /**
   * 构建响应.
   *
   * @param resource 资源
   * @return 结果
   */
  public static ResponseEntity<Resource> buildResponse(Resource resource) {
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(resource);
  }


  /**
   * 创建excel数据.
   *
   * @param header 表头
   * @param data   数据
   * @return excel
   */
  public static Resource getExcelResource(String[] header,
                                          List<String[]> data) {
    try (XSSFWorkbook sheets = new XSSFWorkbook()) {
      XSSFSheet sheet = sheets.createSheet();
      // header
      XSSFRow headerRow = sheet.createRow(0);
      for (int i = 0; i < header.length; i++) {
        XSSFCell cell = headerRow.createCell(i);
        cell.setCellType(CellType.STRING);
        cell.setCellValue(1);
      }

      for (int i = 0; i < data.size(); i++) {
        XSSFRow row = sheet.createRow(i + 1);
        String[] rowData = data.get(i);
        for (int j = 0; j < rowData.length; j++) {
          String cellValue = rowData[j];
          XSSFCell cell = row.createCell(j);
          cell.setCellValue(cellValue);
        }
      }

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      sheets.write(byteArrayOutputStream);
      return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
