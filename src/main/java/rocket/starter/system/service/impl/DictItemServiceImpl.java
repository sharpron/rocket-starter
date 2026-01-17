package rocket.starter.system.service.impl;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rocket.starter.common.AbstractService;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.DictItem;
import rocket.starter.system.repo.DictItemRepo;
import rocket.starter.system.repo.DictRepo;
import rocket.starter.system.service.DictItemService;

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
  private final DictRepo dictRepo;


  @Override
  protected BaseRepo<DictItem> getBaseRepo() {
    return dictItemRepo;
  }

  @Override
  public List<DictItem> findByDictName(String dictName) {
    return dictRepo.findByName(dictName)
        .map(e -> dictItemRepo.findByDictId(e.getId()))
        .orElse(Collections.emptyList());
  }
}
