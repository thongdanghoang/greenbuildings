import {Component} from '@angular/core';
import {DynamicDialogRef} from 'primeng/dynamicdialog';

@Component({
  selector: 'app-create-dashboard',
  templateUrl: './create-dashboard.component.html',
  styleUrl: './create-dashboard.component.css'
})
export class CreateDashboardComponent {
  powerBiEmbedded: string | undefined;

  constructor(private readonly ref: DynamicDialogRef) {}

  isValid(): boolean {
    return Boolean(this.extractTitle) && Boolean(this.extractSource);
  }

  submit(): void {
    if (this.isValid()) {
      this.ref.close({
        source: this.extractSource,
        title: this.extractTitle
      });
    }
  }

  get extractTitle(): string | undefined {
    return this.powerBiEmbedded?.match(/title="([^"]+)"/)?.[1];
  }

  get extractSource(): string | undefined {
    return this.powerBiEmbedded?.match(/src="([^"]+)"/)?.[1];
  }

  close(): void {
    this.ref.close();
  }
}
