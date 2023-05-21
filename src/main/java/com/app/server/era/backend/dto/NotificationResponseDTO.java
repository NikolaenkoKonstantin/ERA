package com.app.server.era.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private int id;

    private String cardNumber;

    private LocalDateTime dateTime;
}
