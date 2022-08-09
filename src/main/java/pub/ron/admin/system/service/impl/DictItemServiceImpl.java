package pub.ron.admin.system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.BaseRepo;
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
@RequiredArgsConstructor
public class DictItemServiceImpl extends AbstractService<DictItem>
    implements DictItemService {

  private final DictItemRepo dictItemRepo;


  @Override
  protected BaseRepo<DictItem> getBaseRepo() {
    return dictItemRepo;
  }
}
