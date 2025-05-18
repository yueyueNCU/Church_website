package org.kangning.church.common.exception.churchRole;

public class DefaultChurchRoleCannotBeModifiedException extends RuntimeException {
    public DefaultChurchRoleCannotBeModifiedException(){
        super("預設角色不可更改");
    }
}
