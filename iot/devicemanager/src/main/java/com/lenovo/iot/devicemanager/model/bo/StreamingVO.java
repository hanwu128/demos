package com.lenovo.iot.devicemanager.model.bo;

import java.io.Serializable;

/**
 * Created by root on 2017/8/22.
 */
public class StreamingVO implements Serializable
{
    private static final long serialVersionUID = -2997153735796272771L;

    private Long id;

    private String streamingname;

    private String streamingdesc;

    private Long createtimeat;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreamingname() {
        return streamingname;
    }

    public void setStreamingname(String streamingname) {
        this.streamingname = streamingname;
    }

    public String getStreamingdesc() {
        return streamingdesc;
    }

    public void setStreamingdesc(String streamingdesc) {
        this.streamingdesc = streamingdesc;
    }

    public Long getCreatetimeat() {
        return createtimeat;
    }

    public void setCreatetimeat(Long createtimeat) {
        this.createtimeat = createtimeat;
    }
}
