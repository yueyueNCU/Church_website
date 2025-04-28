package org.kangning.church.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ChurchRole {
    private Long ChurchId;
    private List<Role> roles;
}
