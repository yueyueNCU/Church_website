package org.kangning.church.churchRole.application.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.kangning.church.churchRole.application.port.in.ChurchRoleUseCase;
import org.kangning.church.churchRole.application.port.out.ChurchRoleRepositoryPort;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.churchRole.domain.Permission;
import org.kangning.church.common.exception.churchRole.ChurchRoleNameDuplicatedInTheChurchException;
import org.kangning.church.common.exception.churchRole.ChurchRoleNotFoundException;
import org.kangning.church.common.exception.churchRole.DefaultChurchRoleCannotBeModifiedException;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.ChurchRoleId;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class ChurchRoleService implements ChurchRoleUseCase {

    ChurchRoleRepositoryPort churchRoleRepository;

    @Override
    public ChurchRole createChurchRole(ChurchId churchId, String name, boolean isDefault) {
        if (churchRoleRepository.existsByChurchIdAndName(churchId, name)) {
            throw new ChurchRoleNameDuplicatedInTheChurchException();
        }

        ChurchRole newRole = new ChurchRole(
                null,
                churchId,
                name,
                isDefault,
                Set.of()
        );

        return churchRoleRepository.save(newRole);
    }

    @Override
    public Set<ChurchRole> getChurchRoles(ChurchId churchId) {
        return churchRoleRepository.findAllByChurchId(churchId);
    }

    @Override
    public Set<ChurchRole> getDefaultRoles(ChurchId churchId) {
        return churchRoleRepository.findDefaultRolesByChurchId(churchId);
    }

    @Override
    public void deleteChurchRole(ChurchRoleId roleId) {
        ChurchRole role = churchRoleRepository.findById(roleId)
                .orElseThrow(ChurchRoleNotFoundException::new);

        if (role.isDefault()) {
            throw new DefaultChurchRoleCannotBeModifiedException();
        }

        churchRoleRepository.deleteById(roleId);
    }

    @Override
    public void updateRolePermissions(ChurchRoleId roleId, Set<Permission> permissions) {
        ChurchRole role = churchRoleRepository.findById(roleId)
                .orElseThrow(ChurchRoleNotFoundException::new);

        if (role.isDefault()) {
            throw new DefaultChurchRoleCannotBeModifiedException();
        }

        ChurchRole updated = new ChurchRole(
                role.getId(),
                role.getChurchId(),
                role.getName(),
                role.isDefault(),
                permissions // ⬅️ 這裡直接取代整組權限
        );

        churchRoleRepository.save(updated);
    }

    @Override
    public void renameChurchRole(ChurchRoleId roleId, String newName) {
        ChurchRole role = churchRoleRepository.findById(roleId)
                .orElseThrow(ChurchRoleNotFoundException::new);;

        if (role.isDefault()) {
            throw new DefaultChurchRoleCannotBeModifiedException();
        }

        ChurchRole updatedRole = new ChurchRole(
                role.getId(),
                role.getChurchId(),
                newName,
                role.isDefault(),
                role.getPermissions()
        );

        churchRoleRepository.save(updatedRole);
    }
}
