package com.example.demo.partitioner;

import com.example.demo.SpringbatchApp;
import com.example.demo.model.Transaction;
import com.example.demo.service.ComplexJsonRecordSeparatorPolicy;
import com.example.demo.service.TransactionLineMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;

@Configuration
@EnableBatchProcessing
public class SpringbatchPartitionConfig {

    @Autowired
    ResourcePatternResolver resoursePatternResolver;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean(name = "partitionerJob")
    public Job partitionerJob(){
        return jobs.get("partitionerJob")
          .start(partitionStep())
          .build();
    }

    @Bean
    public Step partitionStep(){
        return steps.get("partitionStep")
          .partitioner("slaveStep", partitioner())
          .step(slaveStep())
          .taskExecutor(taskExecutor())
          .build();
    }

    @Bean
    public Step slaveStep(){
        return steps.get("slaveStep")
          .<Transaction, Transaction>chunk(1)
          .reader(itemReader(null))
          .writer(itemWriter())
          .build();
    }

    @Bean
    public CustomMultiResourcePartitioner partitioner() {
        CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner();
        Resource[] inputResources;
        for(String arg : SpringbatchApp.ARGS) {
            try {
                inputResources = resoursePatternResolver.getResources(arg);
                File file = new File(arg);
                String filePath = file.getCanonicalPath();
                partitioner.setPath(filePath);
                partitioner.setResources(inputResources);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return partitioner;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Transaction> itemReader(@Value("#{stepExecutionContext[fileName]}") String filename){
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filename));
        if (filename.contains(".json")) reader.setRecordSeparatorPolicy(transactionRecordSeparatorPolicy());
        reader.setLineMapper(new TransactionLineMapper(filename));
        return reader;
    }

    @Bean
    @StepScope
    public RecordSeparatorPolicy transactionRecordSeparatorPolicy() {
        return new ComplexJsonRecordSeparatorPolicy();
    }

	@Bean
    @StepScope
	public ItemWriter itemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setQueueCapacity(5);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}