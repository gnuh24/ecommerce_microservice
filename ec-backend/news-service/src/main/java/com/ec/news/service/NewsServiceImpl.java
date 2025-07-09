package com.ec.news.service;

import com.ec.news.entity.News;
import com.ec.news.exceptions.business.NewsNotFoundException;
import com.ec.news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
	
	@Autowired
	private NewsRepository newsRepository;
	
	@Override
	public Page<News> getPublishedNews(Pageable pageable) {
		return newsRepository.findAllByIsDeletedFalseAndIsPublishedTrue(pageable);
	}
	
	@Override
	public List<News> getHighlightedNews() {
		return newsRepository .findAllByHighlightTrueAndIsDeletedFalseAndIsPublishedTrueOrderByCreatedAtDesc();
	}
	
	@Override
	public News getNewsDetailForUser(String id) {
		return newsRepository.findByIdAndIsDeletedFalseAndIsPublishedTrue(id).orElseThrow(
		    () -> new NewsNotFoundException(id)
		);
	}
	
}
