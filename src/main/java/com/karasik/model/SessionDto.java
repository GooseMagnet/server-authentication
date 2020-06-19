package com.karasik.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class SessionDto {

    public SessionDto(String sessionId, String userId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public SessionDto() {
    }

    @Setter
    @Getter
    private String sessionId;

    @Setter
    @Getter
    private String userId;

}
