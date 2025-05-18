package org.kangning.church.common.exception.churchRole;

public class ChurchRoleNameDuplicatedInTheChurchException extends RuntimeException {
    public ChurchRoleNameDuplicatedInTheChurchException(){
        super("教會角色已經存在");
    }
}
