package org.kangning.church.common.exception.membership;

public class MembershipAlreadyExistsException extends RuntimeException {
    public MembershipAlreadyExistsException(){
        super("會籍已經存在");
    }
}
