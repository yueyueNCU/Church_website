package org.kangning.church.membership.application.port.out;

import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.domain.Membership;

import java.util.List;
import java.util.Optional;

public interface MembershipRepositoryPort {
    /** 新增或更新 Membership（apply, approve, update role 都用得到） */
    Membership save(Membership membership);

    /** 查詢特定教會中指定使用者的 Membership */
    Optional<Membership> findByChurchIdAndUserId(ChurchId churchId, UserId userId);

    /** 查詢某教會所有 Membership（通常只回傳 APPROVED） */
    List<Membership> findAllByChurchId(ChurchId churchId);

    /** 刪除 Membership（讓使用者退出或被移除） */
    void deleteByChurchIdAndUserId(ChurchId churchId, UserId userId);

    List<Membership> findAllByUserId(UserId userId);

    List<Membership> findApprovedByUserId(UserId userId);

    void deleteByAll();
}
