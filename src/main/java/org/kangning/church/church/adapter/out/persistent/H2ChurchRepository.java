package org.kangning.church.church.adapter.out.persistent;

import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface H2ChurchRepository extends JpaRepository<ChurchEntity,Long>{
    boolean existsByName(String name);

    @Query("select c from ChurchEntity c where lower(c.name) like lower(concat('%', :kw, '%'))")
    Page<ChurchEntity> searchByNameContaining(@Param("kw") String keyword, Pageable pageable);
}
