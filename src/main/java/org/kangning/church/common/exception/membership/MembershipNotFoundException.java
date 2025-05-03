package org.kangning.church.common.exception.membership;

public class MembershipNotFoundException extends RuntimeException {
    public MembershipNotFoundException(){
        super("找不到該使用者在此教會的成員資料");
    }
}
