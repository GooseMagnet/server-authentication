package com.karasik.util.helpers;

import com.karasik.model.SessionDto;

public class SessionHelper {

    private static ThreadLocal<SessionDto> session = new ThreadLocal<>();

    public static void createSession(SessionDto sessionDto) {
        session.set(sessionDto);
    }

    public static SessionDto getSession() {
        return session.get();
    }

    public static void destroySession() {
        session.remove();
    }
}
