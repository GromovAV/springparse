package com.example.demo.service;

import org.springframework.batch.item.file.separator.JsonRecordSeparatorPolicy;
import org.springframework.util.StringUtils;

public class ComplexJsonRecordSeparatorPolicy extends JsonRecordSeparatorPolicy {
    private boolean isEndLine;

    @Override
    public boolean isEndOfRecord(String line) {
        isEndLine = StringUtils.countOccurrencesOf(line, "{") == StringUtils.countOccurrencesOf(line, "}")
                && (line.trim().endsWith("}") || line.trim().endsWith(",") || line.trim().endsWith("]"));
        return isEndLine;
    }

    @Override
    public String postProcess(String record) {
        if (isEndLine) {
            if (record.startsWith("[")) record = record.substring(1);
            if ((record.endsWith("]") || record.endsWith(","))) record = record.substring(0, record.length() - 1);
        }
        return super.postProcess(record);
    }
}