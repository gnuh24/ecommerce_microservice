import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../product.service';
import { Product, Page } from '../product.dto';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  currentPage: number = 0;
  pageSize: number = 10;
  totalElements: number = 0;
  searchQuery: string = '';

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.currentPage = params['page'] ? +params['page'] : 0;
      this.pageSize = params['size'] ? +params['size'] : 10;
      this.searchQuery = params['search'] || '';
      this.loadProducts();
    });
  }

  loadProducts(): void {
    this.productService.getPublicProducts(this.currentPage, this.pageSize, this.searchQuery)
      .subscribe((data: Page<Product>) => {
        this.products = data.content;
        this.totalElements = data.totalElements;
        this.currentPage = data.number;
        this.pageSize = data.size;
      });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.updateQueryParams();
  }

  onSearch(): void {
    this.currentPage = 0; // Reset to first page on new search
    this.updateQueryParams();
  }

  updateQueryParams(): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: this.currentPage,
        size: this.pageSize,
        search: this.searchQuery || null // Remove search param if empty
      },
      queryParamsHandling: 'merge'
    });
  }

  get totalPages(): number[] {
    return Array(Math.ceil(this.totalElements / this.pageSize)).fill(0).map((x, i) => i);
  }
}