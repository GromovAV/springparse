package com.example.demo.partitioner;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class CustomMultiResourcePartitioner implements Partitioner {

    private static final String DEFAULT_KEY_NAME = "fileName";

    private static final String PARTITION_KEY = "partition";

    private Resource[] resources = new Resource[0];

    private String keyName = DEFAULT_KEY_NAME;

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>(gridSize);
        int i = 0;
        for (Resource resource : resources) {
            ExecutionContext context = new ExecutionContext();
            Assert.state(resource.exists(), "Resource does not exist: " + resource);
            context.putString(keyName, resource.getFilename());
            map.put(PARTITION_KEY + i, context);
            i++;
        }
        return map;
    }
}