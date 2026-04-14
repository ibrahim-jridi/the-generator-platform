package com.pfe.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

  @Override
  @Query("SELECT e FROM #{#entityName} e WHERE e.deleted = false")
  List<T> findAll();

  @Override
  @Query("SELECT e FROM #{#entityName} e WHERE e.deleted = false")
  Page<T> findAll(Pageable pageable);

  @Override
  @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.deleted = false")
  Optional<T> findById(@Param("id") ID id);

  @Override
  @Modifying
  @Query("UPDATE #{#entityName} e SET e.deleted = true WHERE e.id = :id")
  void deleteById(@Param("id") ID id);
}

