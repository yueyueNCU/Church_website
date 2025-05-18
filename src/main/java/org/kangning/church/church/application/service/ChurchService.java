package org.kangning.church.church.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.church.application.port.in.ChurchResult;
import org.kangning.church.church.application.port.in.CreateChurchCommand;
import org.kangning.church.churchRole.adapter.out.JpaChurchRoleRepository;
import org.kangning.church.churchRole.application.port.out.ChurchRoleRepositoryPort;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.churchRole.domain.Permission;
import org.kangning.church.common.exception.membership.MembershipAlreadyExistsException;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.church.application.port.in.ChurchUseCase;
import org.kangning.church.church.application.port.out.ChurchRepositoryPort;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.exception.church.ChurchNameDuplicateException;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.application.port.out.MembershipRepositoryPort;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.membership.domain.Membership;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class ChurchService implements ChurchUseCase {

    private final ChurchRepositoryPort churchRepository;

    private final MembershipRepositoryPort membershipRepository;

    private final ChurchRoleRepositoryPort churchRoleRepository;

    private final JpaChurchRoleRepository jpaChurchRoleRepository;

    private Set<ChurchRole> createDefaultRoles(ChurchId churchId){
        ChurchRole leader = new ChurchRole(
                null,
                churchId,
                "領袖",
                true,
                Set.of(Permission.ALL_PERMISSION)
        );
        return Set.of(leader);
    }

    @Override
    public Church createChurch(UserId userId, CreateChurchCommand command) {
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
        Set<ChurchRole> defaultUser= createDefaultRoles(church.getId());

        Set<ChurchRole> savedDefaultUser= churchRoleRepository.saveAll(defaultUser);

        Membership membership= new Membership(
                null,
                church.getId(),
                userId,
                savedDefaultUser,
                ChurchMemberStatus.APPROVED
        );
        membershipRepository.save(membership);
        return church;
    }

    @Override
    public List<ChurchResult> getMyChurches(UserId userId) {
        List<Membership> memberships = membershipRepository.findApprovedByUserId(userId);

        // 2. 取出所有 ChurchId
        List<ChurchId> churchIds = memberships.stream()
                .map(Membership::getChurchId)
                .toList();

        // 3. 查詢這些教會資訊
        List<Church> churches = churchRepository.findAllByIds(churchIds);

        // 4. 組合成 DTO 回傳
        return churches.stream()
                .map(ch -> new ChurchResult(
                        ch.getId(),
                        ch.getName(),
                        ch.getAddress(),

                        ch.getDescription(),
                        ch.getCreatedAt()
                ))
                .toList();
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
