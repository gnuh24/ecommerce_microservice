import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { CategoryService } from '../../core/services/category.service';
import { BrandService } from '../../core/services/brand.service';
declare var $: any;

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, SharedModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  inputModel: any;

  constructor(private categoryService: CategoryService, private brandService: BrandService  ) {}

  ngOnInit() {
    this.categoryService.getCategoriesNoPagination().subscribe((categories) => {
      console.log(categories);
    });
    this.brandService.getBrandsNoPagination().subscribe((brands) => {
      console.log(brands);
    });
  }

  ngAfterViewInit(): void {
    const $carousel = $('.hot-offers-carousel');
    console.log('jQuery?', typeof $ === 'function'); // phải true
    console.log('Slick?', typeof $carousel.slick === 'function'); // phải true

    if ($carousel.length && typeof $carousel.slick === 'function') {
      $carousel.slick({
        slidesToShow: 4,
        slidesToScroll: 1,
        dots: true,
        autoplay: true,
      });
    } else {
      console.error('Slick not loaded properly.');
    }
  }
}