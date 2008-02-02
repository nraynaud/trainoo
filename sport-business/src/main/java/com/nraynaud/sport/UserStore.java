package com.nraynaud.sport;

public interface UserStore {
    User fetchUser(Long id) throws UserNotFoundException;

    User fetchRememberedUser(final String rememberCookie) throws UserNotFoundException;
}
