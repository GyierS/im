package com.zsgwsjj.im.model;

import java.io.Serializable;

/**
 * @author : jiang
 * @time : 2018/4/25 14:14
 */
public class IMMessage implements Serializable{

    private long id;
    private long from;
    private long to;
    private String body;
    private long createTime;

    public IMMessage() {
    }

    public IMMessage(long id, long from, long to, String body, long createTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.body = body;
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public IMMessage setId(long id) {
        this.id = id;
        return this;
    }

    public long getFrom() {
        return from;
    }

    public IMMessage setFrom(long from) {
        this.from = from;
        return this;
    }

    public long getTo() {
        return to;
    }

    public IMMessage setTo(long to) {
        this.to = to;
        return this;
    }

    public String getBody() {
        return body;
    }

    public IMMessage setBody(String body) {
        this.body = body;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public IMMessage setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public String toString() {
        return "Mail{" +
                ", from=" + from +
                ", to=" + to +
                ", body='" + body + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
