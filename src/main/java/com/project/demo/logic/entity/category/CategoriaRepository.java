package com.project.demo.logic.entity.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT c FROM Categoria c Where c.name = ?1")
    public Categoria findByName(String name);

    public Page<Categoria> findAll(Pageable pageable);
}
