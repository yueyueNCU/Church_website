package org.kangning.church.churchRole.application.port.out;

import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.ChurchRoleId;

import java.util.Optional;
import java.util.Set;

public interface ChurchRoleRepositoryPort {
    /** 根據教會 ID 查出所有角色 */
    Set<ChurchRole> findAllByChurchId(ChurchId churchId);

    /** 查出教會的 default role（預設角色） */
    Set<ChurchRole> findDefaultRolesByChurchId(ChurchId churchId);

    /** 儲存一個新角色 */
    ChurchRole save(ChurchRole role);


    Set<ChurchRole> saveAll(Set<ChurchRole> roles);
    /** 刪除角色 */
    void deleteById(ChurchRoleId id);

    /** 根據 ID 查詢角色 */
    Optional<ChurchRole> findById(ChurchRoleId id);

    /** 查詢教會中是否已存在某個名稱的角色（防止重複） */
    boolean existsByChurchIdAndName(ChurchId churchId, String name);
}
