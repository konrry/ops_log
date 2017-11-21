package net.galvin.ops.log.job;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 任务调度配置
 */
@Configuration
@ComponentScan("net.galvin.ops.log.job")
@EnableScheduling
public class TaskSchedulerConfig {

}
