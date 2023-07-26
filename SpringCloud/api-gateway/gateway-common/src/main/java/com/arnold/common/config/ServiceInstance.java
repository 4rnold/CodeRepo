package com.arnold.common.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class ServiceInstance implements Serializable {

    private String serviceInstanceId;

    private String uniqueId;

    private String ip;

    private int port;

    private String tags;

    private Integer weight;

    private long registerTime;

    private boolean enable = true;

    private String version;

    private boolean gray;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceInstance that = (ServiceInstance) o;
        return Objects.equals(serviceInstanceId, that.serviceInstanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceInstanceId);
    }
}
