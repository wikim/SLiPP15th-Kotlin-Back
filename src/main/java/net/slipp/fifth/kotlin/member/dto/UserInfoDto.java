package net.slipp.fifth.kotlin.member.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String password;
}
