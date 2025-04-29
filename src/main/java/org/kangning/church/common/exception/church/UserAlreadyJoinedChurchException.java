package org.kangning.church.common.exception.church;

public class UserAlreadyJoinedChurchException extends RuntimeException {
    public UserAlreadyJoinedChurchException(){
        super("使用者已經加入教會");
    }
}
