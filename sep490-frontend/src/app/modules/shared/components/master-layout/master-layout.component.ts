import {Location} from '@angular/common';
import {Component, input} from '@angular/core';

@Component({
  selector: 'master-layout',
  templateUrl: './master-layout.component.html',
  styleUrl: './master-layout.component.css'
})
export class MasterLayoutComponent {
  showBack = input(false);
  title = input.required<string>();
  styleClass = input<string | undefined>(undefined);

  constructor(private readonly location: Location) {}

  goBack(): void {
    this.location.back();
  }
}
