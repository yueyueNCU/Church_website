package org.kangning.church.church.application.port.in;

import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;

import java.util.List;

public interface ChurchUseCase {
    Church createChurch(UserId userId, CreateChurchCommand command);
    List<ChurchResult> getMyChurches(UserId userId);
    List<ChurchResult> searchByNameContaining(String keyword, int size, int offset);
}
