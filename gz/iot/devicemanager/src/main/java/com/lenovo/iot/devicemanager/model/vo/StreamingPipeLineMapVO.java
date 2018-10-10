package com.lenovo.iot.devicemanager.model.vo;

import java.io.Serializable;

/**
 * Created by root on 2017/8/29.
 */
public class StreamingPipeLineMapVO implements Serializable {

    private static final long serialVersionUID = 2768501636673911947L;

    private String createstreamingpipelinemapjson;


    public String getCreatestreamingpipelinemapjson() {
        return createstreamingpipelinemapjson;
    }

    public void setCreatestreamingpipelinemapjson(String createstreamingpipelinemapjson) {
        this.createstreamingpipelinemapjson = createstreamingpipelinemapjson;
    }
}
