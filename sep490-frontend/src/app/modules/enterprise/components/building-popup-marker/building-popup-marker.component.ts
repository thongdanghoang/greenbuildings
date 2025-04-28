import {Component, Input} from '@angular/core';
import {Building} from '../../../../models/enterprise';

@Component({
  selector: 'app-building-popup-marker',
  templateUrl: './building-popup-marker.component.html',
  styleUrl: './building-popup-marker.component.css'
})
export class BuildingPopupMarkerComponent {
  @Input({required: true}) building!: Building;
}
