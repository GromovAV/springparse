package com.example.demo;

import com.example.demo.partitioner.SpringbatchPartitionConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringbatchApp {
    public static String[] ARGS;

    public static void main(String[] args) {
        ARGS = args;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SpringbatchPartitionConfig.class);
        context.refresh();

        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        Job job = (Job) context.getBean("partitionerJob");
        try {
          JobExecution execution = jobLauncher.run(job, new JobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}