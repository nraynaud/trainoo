package com.nraynaud.sport;

import java.io.Serializable;

public interface User extends Serializable {
    public boolean checkPassword(final String candidate);

    Long getId();

    UserString getName();

    UserString getTown();

    UserString getDescription();

    UserString getWebSite();

    String getRememberToken();

    UserString getEmail();

    UserString getNikePluEmail();

    UserString getNikePlusPassword();

    String getNikePlusId();

    Long getFacebookId();
}
