import { Component, OnInit, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { ProductService } from '../../core/services/product.service';
declare var $: any;

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, SharedModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, AfterViewChecked {
  inputModel: any;
  products: any[] = [];
  private slickInitialized = false;

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.productService.getPublicProducts(0, 12).subscribe({
      next: (response) => {
        this.products = response.data.content;
        this.slickInitialized = false; // reset để ngAfterViewChecked chạy lại
      },
      error: (err: any) => {
        console.error('Error fetching products:', err);
      }
    });
  }

  ngAfterViewChecked() {
    if (this.products.length && !this.slickInitialized) {
      const $carousel = $('.hot-offers-carousel');
      if ($carousel.length && typeof $.fn.slick === 'function') {
        if ($carousel.hasClass('slick-initialized')) {
          $carousel.slick('unslick');
        }
        $carousel.slick({
          slidesToShow: 3 ,
          slidesToScroll: 1,
          dots: true,
          autoplay: true,
          responsive: [
            {
              breakpoint: 1024,
              settings: {
                slidesToShow: 3,
                slidesToScroll: 1,
                infinite: true,
                dots: true
              }
            },
            {
              breakpoint: 768,
              settings: {
                slidesToShow: 2,
                slidesToScroll: 1
              }
            },
            {
              breakpoint: 480,
              settings: {
                slidesToShow: 1,
                slidesToScroll: 1
              }
            }
          ]
        });
        this.slickInitialized = true;
      }
    }
  }
}