import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { StorageService, RequestStatus } from '../services/storage.service';

@Component({
  selector: 'app-create-request',
  templateUrl: './create-request.component.html',
  styleUrls: ['./create-request.component.css'],
  animations: [
    trigger('expandedPanel', [
      state('initial', style({ height: 0 })),
      state('expanded', style({ height: '*' })),
      transition('initial <=> expanded', animate('0.2s')),
    ]),
  ]
})


export class CreateRequestComponent {
  constructor(public storage: StorageService) { }

  statuses: string[] = [];
  values: number[] = [];

  ngOnInit(){
    this.statuses = Object.values(RequestStatus);
    this.values = [this.statuses.length];
    for (let index = 0; index < this.statuses.length; index++) {
      this.values[index] = index;  
    }
  }
}
