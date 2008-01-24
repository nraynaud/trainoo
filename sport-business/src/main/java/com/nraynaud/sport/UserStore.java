package com.nraynaud.sport;

public interface UserStore {
    User fetchUser(Long id) throws UserNotFoundException;
}
