package com.ec.news.service;

import com.ec.news.entity.News;
import com.ec.news.exceptions.business.NewsNotFoundException;
import com.ec.news.repository.NewsRepository;
import com.ec.news.specification.NewsSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
	public News getPublicNewsById(String id) {
		return newsRepository.findByIdAndIsDeletedFalseAndIsPublishedTrue(id)
		    .orElseThrow( () -> new NewsNotFoundException(id));
	}
	
	@Override
	public News getNewsById(String id) {
		return newsRepository.findByIdAndIsDeletedFalse(id)
		    .orElseThrow(() -> new NewsNotFoundException(id));
	}
	
	
	@Override
	public Page<News> filterNewsForAdmin(String keyword, Boolean highlight, Boolean isPublished, Pageable pageable) {
		Specification<News> spec = Specification
		    .where(NewsSpecification.isNotDeleted())
		    .and(NewsSpecification.hasHighlight(highlight))
		    .and(NewsSpecification.hasTitleLike(keyword))
		    .and(NewsSpecification.hasIsPublished(isPublished));
		
		return newsRepository.findAll(spec, pageable);
	}
	
	@Override
	public void deleteNewsById(String id) {
		News news = getNewsById(id);
		
		news.setIsDeleted(true);
		news.setDeletedAt(LocalDateTime.now());
		
		newsRepository.save(news);
	}
	
	
}
