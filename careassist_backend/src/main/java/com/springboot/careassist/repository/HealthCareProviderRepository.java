package com.springboot.careassist.repository;

import com.springboot.careassist.dto.response.HealthCareProviderRespDto;
import com.springboot.careassist.model.HealthCareProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthCareProviderRepository extends JpaRepository<HealthCareProvider,Long> {

    @Query("""
select h.id,
h.name,
u.isActive,
u.createdAt
from HealthCareProvider h
JOIN h.user u
where u.isActive = ?1
""")
    Page<HealthCareProviderRespDto> findActiveAll(boolean isActive, Pageable pageable);

    @Query("""
select h
from HealthCareProvider  h
JOIN h.user u
where u.username = ?1
""")
    HealthCareProvider getByUsername(String providerUsername);
}
