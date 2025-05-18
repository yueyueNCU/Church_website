package org.kangning.church.churchRole.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {
    // 👥 MEMBER 管理
    VIEW_MEMBERS(PermissionGroup.MEMBER, "查看成員"),
    ADD_MEMBER(PermissionGroup.MEMBER, "新增成員"),
    REMOVE_MEMBER(PermissionGroup.MEMBER, "移除成員"),
    APPROVE_MEMBER(PermissionGroup.MEMBER, "核准加入"),
    REJECT_MEMBER(PermissionGroup.MEMBER, "拒絕加入"),
    ASSIGN_ROLE(PermissionGroup.MEMBER, "分派角色"),

    // 🧩 ROLE 管理
    VIEW_ROLES(PermissionGroup.ROLE, "查看角色"),
    ADD_ROLE(PermissionGroup.ROLE, "新增角色"),
    DELETE_ROLE(PermissionGroup.ROLE, "刪除角色"),
    UPDATE_ROLE(PermissionGroup.ROLE, "調整角色權限"),
    RENAME_ROLE(PermissionGroup.ROLE, "修改角色名稱"),

    // 🏠 CHURCH 設定
    EDIT_CHURCH_PROFILE(PermissionGroup.CHURCH, "編輯教會資訊"),

    // 🎯 全權限
    ALL_PERMISSION(PermissionGroup.CHURCH, "擁有所有權限");

    private final PermissionGroup group;
    private final String description;
}

