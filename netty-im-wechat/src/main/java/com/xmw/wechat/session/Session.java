package com.xmw.wechat.session;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 会话信息
 *
 * @author mingwei.xia
 * @date 2018/10/15 9:37
 * @since V1.0
 */
@Data
@ToString
@NoArgsConstructor
public class Session {
    private String userId;
    private String userName;

    public Session(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
