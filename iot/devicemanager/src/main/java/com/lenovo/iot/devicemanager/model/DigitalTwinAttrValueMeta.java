package com.lenovo.iot.devicemanager.model;

public class DigitalTwinAttrValueMeta {
    private Long id = 0L;
    private Double value = 0.0d;
    private Long valuetimestamp = 0L;

    public DigitalTwinAttrValueMeta(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getValuetimestamp() {
        return valuetimestamp;
    }

    public void setValuetimestamp(Long valuetimestamp) {
        this.valuetimestamp = valuetimestamp;
    }
}
