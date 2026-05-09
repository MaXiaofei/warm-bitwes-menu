package com.warmbitwes.menu.security;

public final class SecurityContextHolder {
    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    private SecurityContextHolder() {
    }

    public static void set(LoginUser user) {
        HOLDER.set(user);
    }

    public static LoginUser get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
