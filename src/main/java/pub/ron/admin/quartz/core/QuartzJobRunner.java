/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package pub.ron.admin.quartz.core;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pub.ron.admin.quartz.domain.QuartzJob;
import pub.ron.admin.quartz.repo.QuartzJobRepo;

/**
 * @author ron 2021-02-09
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QuartzJobRunner implements ApplicationRunner {

  private final QuartzJobRepo quartzJobRepo;

  private final QuartzJobManager quartzJobManager;

  /**
   * 系统启动时需要重新启动job
   *
   * @param applicationArguments applicationArguments
   */
  @Override
  public void run(ApplicationArguments applicationArguments) {
    log.info("--------------------启动定时任务---------------------");
    List<QuartzJob> quartzJobs = quartzJobRepo.findByEnabledIs(true);
    quartzJobs.forEach(quartzJobManager::addJob);
    log.info("--------------------定时任务启动完成---------------------");
  }
}
