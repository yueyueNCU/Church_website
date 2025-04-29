package org.kangning.church.church.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.church.application.port.in.ChurchResult;
import org.kangning.church.church.application.port.in.CreateChurchCommand;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.church.application.port.in.ChurchUseCase;
import org.kangning.church.church.application.port.out.ChurchRepositoryPort;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.exception.church.ChurchNameDuplicateException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChurchService implements ChurchUseCase {


    private final ChurchRepositoryPort churchRepository;

    private final UserRepositoryPort userRepository;
    @Override
    public ChurchId createChurch(String username, CreateChurchCommand command) {
        if(churchRepository.existsByName(command.name())) {
            throw new ChurchNameDuplicateException();
        }

        Church church =churchRepository.save(new Church(
                null,
                command.name(),
                command.address(),
                command.description(),
                null
            )
        );
//        UserId userId = userRepository.findByUsername(username)
//                .orElseThrow(UserNotFoundException::new)
//                .getId();
        //add Leader membership to the username
        return new ChurchId(church.getId().value());
    }

    @Override
    public List<ChurchResult> getMyChurches(String username) {
        //search membershipRepo
        return List.of();
    }

    @Override
    public void joinChurch(String username, ChurchId churchId) {
        //use membershipRepo
    }

    @Override
    public List<ChurchResult> searchByNameContaining(String keyword, int size, int offset) {
        List<Church> result = churchRepository.searchByNameContaining(keyword, size, offset);
        return result.stream().map(
                church->
                        new ChurchResult(
                                new ChurchId(church.getId().value()),
                                church.getName(),
                                church.getAddress(),
                                church.getDescription(),
                                church.getCreatedAt()
                        )
        ).toList();
    }
}
