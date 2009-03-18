package com.nraynaud.sport;

public interface UserStore {
    User fetchUser(Long id) throws UserNotFoundException;

    User fetchUser(String login) throws UserNotFoundException;

    User fetchRememberedUser(final String rememberCookie) throws UserNotFoundException;

    void facebookConnect(User user, Long facebookId) throws NameClashException;

    /**
     * @param facebookId
     * @return null if user not found, of user
     */
    User facebookLogin(Long facebookId);
}
