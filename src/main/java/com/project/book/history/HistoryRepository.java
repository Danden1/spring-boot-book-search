package com.project.book.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History,Integer> {
    public List<History> findByUserId(Integer userId);
    public History findByUserIdAndKeyword(Integer userId, String keyword);
    public List<History> findByUserIdOrderBySearchTimeDesc(Integer userId);
}
