package com.app.server.era.backend.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
    private String elbowKnee;

    private String leftRight;
}
