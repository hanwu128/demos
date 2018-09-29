package com.example.stage.processor.sample;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.example.stage.lib.sample.Errors;

import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.FileRef;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.base.OnRecordErrorException;
import com.streamsets.pipeline.api.base.SingleLaneRecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class SampleProcessor extends SingleLaneRecordProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(SampleProcessor.class);

    public abstract String getConfig();

    @Override
    protected List<ConfigIssue> init() {
        List<ConfigIssue> issues = super.init();
        if (getConfig().equals("invalidValue")) {
            issues.add(
                    getContext().createConfigIssue(
                            Groups.SAMPLE.name(), "config", Errors.SAMPLE_00, "Here's what's wrong..."
                    )
            );
        }
        return issues;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void process(Record record, SingleLaneBatchMaker batchMaker) throws StageException {
        LOG.info("Input record: {}", record);
        FileRef fileRef = record.get("/fileRef").getValueAsFileRef();
        Metadata metadata;
        try {
            metadata = ImageMetadataReader.readMetadata(fileRef.createInputStream(getContext(), InputStream.class));
        } catch (ImageProcessingException | IOException e) {
            String filename = record.get("/fileInfo/filename").getValueAsString();
            LOG.info("Exception getting metadata from {}", filename, e);
            throw new OnRecordErrorException(record, Errors.SAMPLE_02, e);
        }

        for (Directory directory : metadata.getDirectories()) {
            LinkedHashMap<String, Field> listMap = new LinkedHashMap<>();
            for (Tag tag : directory.getTags()) {
                listMap.put(tag.getTagName(), Field.create(tag.getDescription()));
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    LOG.info("ERROR: {}", error);
                }
            }
            record.set("/" + directory.getName(), Field.createListMap(listMap));
        }
        LOG.info("Output record: {}", record);
        batchMaker.addRecord(record);
    }
}