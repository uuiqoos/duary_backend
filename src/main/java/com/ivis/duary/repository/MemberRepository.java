package com.ivis.duary.repository;

import com.ivis.duary.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.username = ?1 and m.password = ?2")
    Optional<Member> findByUsernameAndPassword(String username, String password);

    @Query("select m from Member m where m.username = ?1")
    List<Member> findByUsername(String username);

    Page<Member> findAll(Pageable pageable);

}
