import { Component, AfterViewInit } from '@angular/core';
import { OnInit } from '@angular/core';

declare var $: any;

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, AfterViewInit {
  inputModel: any;

  constructor() {}

  ngOnInit() {}

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
