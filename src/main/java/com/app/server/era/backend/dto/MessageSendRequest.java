package com.app.server.era.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendRequest {
    private String email;
    private String message;

    public MessageSendRequest(String email) {
        this.email = email;
    }
}
