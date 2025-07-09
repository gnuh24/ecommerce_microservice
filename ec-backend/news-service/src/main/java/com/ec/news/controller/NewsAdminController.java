package com.ec.news.controller;

import com.ec.news.api.ApiResponse;
import com.ec.news.dto.NewsDetailDTOForAdmin;
import com.ec.news.dto.NewsListDTOForAdmin;
import com.ec.news.entity.News;
import com.ec.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/news")
public class NewsAdminController {
	
	@Autowired
	private NewsService newsService;
	
	@GetMapping()
	public ResponseEntity<ApiResponse<Page<NewsListDTOForAdmin>>> getAllNews(
	    @RequestParam(required = false) String search,
	    @RequestParam(required = false) Boolean highlight,
	    @RequestParam(required = false) Boolean isPublished,
	    Pageable pageable
	) {
		Page<News> entities = newsService.filterNewsForAdmin(search, highlight, isPublished, pageable);
		Page<NewsListDTOForAdmin> result = entities.map(NewsListDTOForAdmin::toListDTO);
		return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách bài viết thành công", result));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<NewsDetailDTOForAdmin>> getNewsDetail(@PathVariable String id) {
		News entity = newsService.getNewsById(id);
		NewsDetailDTOForAdmin result = NewsDetailDTOForAdmin.toDTO(entity);
		return ResponseEntity.ok(new ApiResponse<>(200, "Lấy chi tiết bài viết thành công", result));
	}
	
	@DeleteMapping("/{newsId}")
	public ResponseEntity<ApiResponse<Void>> deleteNews(@PathVariable String newsId) {
		newsService.deleteNewsById(newsId);
		return ResponseEntity.ok(new ApiResponse<>(200, "Xóa bài viết thành công", null));
	}
	
	
}

