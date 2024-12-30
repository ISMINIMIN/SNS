package minzdev.sns.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmArgs {

    private Integer fromUserId;
    private Integer targetId;

}
