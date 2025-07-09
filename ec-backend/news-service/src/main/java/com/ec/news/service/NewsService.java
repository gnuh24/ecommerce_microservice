package com.ec.news.service;

import com.ec.news.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {
	Page<News> getPublishedNews(Pageable pageable);
	
	List<News> getHighlightedNews();
	News getNewsDetailForUser(String id);
	
}
