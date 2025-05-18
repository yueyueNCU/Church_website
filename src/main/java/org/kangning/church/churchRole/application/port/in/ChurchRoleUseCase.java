package org.kangning.church.churchRole.application.port.in;

import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.churchRole.domain.Permission;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.ChurchRoleId;

import java.util.Set;

public interface ChurchRoleUseCase {
    /** 建立新的角色 */
    ChurchRole createChurchRole(ChurchId churchId, String name, boolean isDefault);

    /** 查詢某教會所有角色 */
    Set<ChurchRole> getChurchRoles(ChurchId churchId);

    /** 查詢某教會的 default 角色（如音控、招待等） */
    Set<ChurchRole> getDefaultRoles(ChurchId churchId);

    /** 刪除角色（不允許刪除 default） */
    void deleteChurchRole(ChurchRoleId roleId);

    void updateRolePermissions(ChurchRoleId roleId, Set<Permission> permissions);

    /** 修改角色名稱 */
    void renameChurchRole(ChurchRoleId roleId, String newName);
}
