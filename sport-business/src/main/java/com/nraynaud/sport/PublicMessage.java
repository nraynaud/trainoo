package com.nraynaud.sport;

public interface PublicMessage extends Message {

    Group getGroup();

    Topic.Kind getTopic();
}
