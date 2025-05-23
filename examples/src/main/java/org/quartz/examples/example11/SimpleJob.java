/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved.
 * Copyright IBM Corp. 2024, 2025
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
 
package org.quartz.examples.example11;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * <p>
 * This is just a simple job that gets fired off many times by example 1
 * </p>
 * 
 * @author Bill Kratzer
 */
public class SimpleJob implements Job {

  private static Logger      _log       = LoggerFactory.getLogger(SimpleJob.class);

  // job parameter
  public static final String DELAY_TIME = "delay time";

  /**
   * Empty constructor for job initialization
   */
  public SimpleJob() {
  }

  /**
   * <p>
   * Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires that
   * is associated with the <code>Job</code>.
   * </p>
   * 
   * @throws JobExecutionException if there is an exception while executing the job.
   */
  public void execute(JobExecutionContext context) throws JobExecutionException {

    // This job simply prints out its job name and the
    // date and time that it is running
    JobKey jobKey = context.getJobDetail().getKey();
    _log.info("Executing job: " + jobKey + " executing at " + new Date());

    // wait for a period of time
    long delayTime = context.getJobDetail().getJobDataMap().getLong(DELAY_TIME);
    try {
      Thread.sleep(delayTime);
    } catch (Exception e) {
      //
    }

    _log.info("Finished Executing job: " + jobKey + " at " + new Date());
  }

}
