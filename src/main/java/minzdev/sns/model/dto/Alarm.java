package minzdev.sns.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import minzdev.sns.model.entity.AlarmArgs;
import minzdev.sns.model.entity.AlarmEntity;
import minzdev.sns.model.enumeration.AlarmType;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {

    private Integer id;
    private User user;
    private AlarmType alarmType;
    private AlarmArgs args;
    private Timestamp createdAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(
                entity.getId(),
                User.fromEntity(entity.getUser()),
                entity.getAlarmType(),
                entity.getArgs(),
                entity.getCreatedAt(),
                entity.getDeletedAt()
        );
    }

}
