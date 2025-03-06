import {Component, OnInit} from '@angular/core';

interface CarouselItem {
  image: string;
  title: string;
  subtitle: string;
  description: string;
}
@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css'
})
export class LandingPageComponent implements OnInit {
  carouselItems!: CarouselItem[];

  constructor() {}

  ngOnInit(): void {
    this.carouselItems = [
      {
        image: 'assets/images/landing-page/carouse.jpg',
        title: 'Green Building',
        subtitle: 'landingPage.carousel.title',
        description: 'landingPage.carousel.description'
      }
    ];
  }
}
