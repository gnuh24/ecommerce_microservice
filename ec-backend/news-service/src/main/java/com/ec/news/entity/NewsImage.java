package com.ec.news.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NewsImage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsId")
    private News news;
}
