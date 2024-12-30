package minzdev.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import minzdev.sns.model.dto.Alarm;
import minzdev.sns.model.entity.AlarmArgs;
import minzdev.sns.model.enumeration.AlarmType;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmResponse {

    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs args;
    private String message;
    private Timestamp createdAt;
    private Timestamp deletedAt;

    public static AlarmResponse from(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getAlarmType().getMessage(),
                alarm.getCreatedAt(),
                alarm.getDeletedAt()
        );
    }

}
