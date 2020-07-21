package com.mapledocs.api.exception;

public class ElasticsearchDaoIndexingException extends Exception {
    public ElasticsearchDaoIndexingException(String msg) {
        super(msg);
    }

    public ElasticsearchDaoIndexingException(String msg, Throwable e) {
        super(msg, e);
    }
}
