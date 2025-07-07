package minzdev.sns.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import minzdev.sns.model.entity.AlarmArgs;
import minzdev.sns.model.enumeration.AlarmType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmEvent {

    private Integer receiverUserId;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;

}
