package rocket.starter.system.dto;

import lombok.Value;

/**
 * 存储结果.
 *
 * @author ron 2022/8/6
 */
@Value
public class StorageResult {

  String rawFileName;

  String path;
}
