package pub.ron.admin.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
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
public class DictServiceImpl extends AbstractService<Dict, DictRepo> implements DictService {


  public DictServiceImpl(DictRepo repository) {
    super(repository);
  }

}
