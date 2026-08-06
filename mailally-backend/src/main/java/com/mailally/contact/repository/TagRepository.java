package com.mailally.contact.repository;

import com.mailally.contact.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByOrganizationIdAndNameAndIsDeletedFalse(Long organizationId, String name);

    List<Tag> findByOrganizationIdAndIsDeletedFalse(Long organizationId);
}
