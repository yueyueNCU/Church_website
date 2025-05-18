package org.kangning.church.common.exception.churchRole;

public class ChurchRoleNotFoundException extends RuntimeException {
    public ChurchRoleNotFoundException(){
        super("查無教會");
    }
}