package pub.ron.admin.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.system.domain.DictItem;
import pub.ron.admin.system.repo.DictItemRepo;
import pub.ron.admin.system.service.DictItemService;

/**
 * dict item service impl.
 *
 * @author ron 2022/8/9
 */
@Service
@Slf4j
public class DictItemServiceImpl extends AbstractService<DictItem, DictItemRepo>
    implements DictItemService {


  public DictItemServiceImpl(DictItemRepo repository) {
    super(repository);
  }

}
