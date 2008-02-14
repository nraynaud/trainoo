package com.nraynaud.sport;

public class UserStringImpl implements UserString {
    private final String string;

    private UserStringImpl(final String string) {
        this.string = string;
    }

    public String toString() {
        return Helper.escaped(string);
    }

    public String nonEscaped() {
        return string;
    }

    public static UserString valueOf(final String string) {
        return string == null ? null : new UserStringImpl(string);
    }
}
