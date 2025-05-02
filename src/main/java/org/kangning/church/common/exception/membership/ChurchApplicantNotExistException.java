package org.kangning.church.common.exception.membership;

public class ChurchApplicantNotExistException extends RuntimeException {
    public ChurchApplicantNotExistException(){
        super("教會申請者不存在");
    }

}
