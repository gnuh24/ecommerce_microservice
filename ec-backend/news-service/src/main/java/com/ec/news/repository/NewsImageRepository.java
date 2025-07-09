package com.ec.news.repository;

import com.ec.news.entity.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsImageRepository extends JpaRepository<NewsImage, Long> {
    List<NewsImage> findAllByNewsId(String newsId);
}
