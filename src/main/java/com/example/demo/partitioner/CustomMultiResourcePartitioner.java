package com.example.demo.partitioner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

public class CustomMultiResourcePartitioner implements Partitioner {

    private static final String DEFAULT_KEY_NAME = "fileName";

    private static final String PARTITION_KEY = "partition";

    private ArrayList<Resource[]> resources = new ArrayList<Resource[]>();

    private String keyName = DEFAULT_KEY_NAME;

    public void setResources(Resource[] resources) {
        this.resources.add(resources);
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>(gridSize);
        int i = 0;
        for (Resource[] resource : resources) {
            for (Resource res: resource) {
                ExecutionContext context = new ExecutionContext();
                context.putString(keyName, res.getFilename());
                map.put(PARTITION_KEY + i, context);
                i++;
            }
        }
        return map;
    }
}