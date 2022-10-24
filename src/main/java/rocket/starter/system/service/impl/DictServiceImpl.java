package rocket.starter.system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rocket.starter.common.AbstractService;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.Dict;
import rocket.starter.system.repo.DictRepo;
import rocket.starter.system.service.DictService;

/**
 * dict service impl.
 *
 * @author ron 2022/8/8
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DictServiceImpl extends AbstractService<Dict> implements DictService {

  private final DictRepo dictRepo;

  @Override
  protected BaseRepo<Dict> getBaseRepo() {
    return dictRepo;
  }
}
