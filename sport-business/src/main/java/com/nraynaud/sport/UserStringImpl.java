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

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStringImpl)) return false;
        final UserStringImpl that = (UserStringImpl) o;
        return string.equals(that.string);
    }

    public int hashCode() {
        return string.hashCode();
    }
}
