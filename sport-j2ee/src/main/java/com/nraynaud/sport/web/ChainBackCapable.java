package com.nraynaud.sport.web;

public interface ChainBackCapable {
    ActionDetail getFromAction();

    ActionDetail getOnErrorAction();

    public void setActionDescription(final ActionDetail actionDescription);
}
