package com.nraynaud.sport;

import java.io.Serializable;

public interface User extends Serializable {
    public boolean checkPassword(final String candidate);

    Long getId();

    String getName();

    String getTown();

    String getDescription();

    String getWebSite();
}
