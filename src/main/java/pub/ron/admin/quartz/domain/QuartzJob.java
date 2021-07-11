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
package pub.ron.admin.quartz.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pub.ron.admin.common.BaseEntity;

/**
 * @author ron 2021-05-31
 */
@Getter
@Setter
@Entity
@Table(name = "quartz_job")
public class QuartzJob extends BaseEntity {

  private String name;

  private String runnableBeanName;

  private String params;

  private String cronExpression;

  private boolean enabled;

  private String personInCharge;

  private String emails;

  private boolean continueWhenFail;

  private String description;
}
