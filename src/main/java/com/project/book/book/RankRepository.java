package com.project.book.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Integer>{
    public Rank findByKeyword(String keyword);
    public List<Rank> findByOrderByCountDesc();
}
