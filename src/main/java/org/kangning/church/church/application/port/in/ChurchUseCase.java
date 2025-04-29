package org.kangning.church.church.application.port.in;

import org.kangning.church.church.adapter.in.dto.ChurchResponse;
import org.kangning.church.church.adapter.in.dto.CreateChurchResponse;
import org.kangning.church.church.adapter.in.dto.CreateChurchRequest;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;

import java.util.List;

public interface ChurchUseCase {
    ChurchId createChurch(String username, CreateChurchCommand command);
    List<ChurchResult> getMyChurches(String username);
    void joinChurch(String username, ChurchId churchId);
    List<ChurchResult> searchByNameContaining(String keyword, int size, int offset);
}
