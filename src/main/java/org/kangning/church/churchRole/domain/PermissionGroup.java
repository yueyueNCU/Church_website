package org.kangning.church.churchRole.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionGroup {
    MEMBER("成員管理"),
    ROLE("角色管理"),
    CHURCH("教會設定");

    private final String description;
}
