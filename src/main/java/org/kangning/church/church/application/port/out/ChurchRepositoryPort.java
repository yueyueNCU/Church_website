package org.kangning.church.church.application.port.out;

import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;

import java.util.List;
import java.util.Optional;

public interface ChurchRepositoryPort {
    /* ========= 基本 CRUD ========= */

    /** 建立或更新 Church；回傳含資料庫主鍵的 Domain 物件 */
    Church save(Church church);

    /** 依 ChurchId 取得教會，若不存在回傳 Optional.empty() */
    Optional<Church> findById(ChurchId id);

    /** 批次查詢；用在「列出我的教會」等場合，可一次把多個 ID 查回 */
    List<Church> findAllByIds(List<ChurchId> ids);

    /** 刪除教會（若之後有需要） */
    void delete(ChurchId id);

    /* ========= 業務相關查詢 ========= */

    /** 檢查名稱是否重複（建立教會前呼叫） */
    boolean existsByName(String name);

    /** 依名稱模糊搜尋（管理後台清單用；可選擇要不要加） */
    List<Church> searchByNameContaining(String keyword, int limit, int offset);
}
