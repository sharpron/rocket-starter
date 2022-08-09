package pub.ron.admin.system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.domain.Dict;
import pub.ron.admin.system.repo.DictRepo;
import pub.ron.admin.system.service.DictService;

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
