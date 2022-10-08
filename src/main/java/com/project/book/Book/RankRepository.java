package com.project.book.Book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Integer>{
    public Rank findByKeyword(String keyword);
}
