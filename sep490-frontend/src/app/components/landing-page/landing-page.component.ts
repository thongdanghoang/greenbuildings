import {Component, OnInit} from '@angular/core';
import {ApplicationService} from '@services/application.service';

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

  constructor(protected readonly applicationService: ApplicationService) {}

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
  protected login(): void {
    this.applicationService.login();
  }
}
