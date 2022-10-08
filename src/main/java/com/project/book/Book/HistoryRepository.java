package com.project.book.Book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History,Integer> {
    public List<History> findByMyUser_userId(Integer userId);
    public History findByMyUser_userIdAndKeyword(Integer userId, String keyword);
    public List<History> findByMyUser_userIdByOrderBySearchTimeAsc(Integer userId);
}
