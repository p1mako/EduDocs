import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { RequestEntity, StorageService } from '../services/storage.service';
import { Router } from '@angular/router';
import { BackendService } from '../services/backend.service';

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.css', '../main/main.component.css'],
  animations: [
    trigger('expandedPanel', [
      state('initial', style({ height: 0 })),
      state('expanded', style({ height: '*' })),
      transition('initial <=> expanded', animate('0.2s')),
    ]),
  ]
})
export class RequestsComponent {

  constructor(private storage: StorageService, private router: Router, private backend: BackendService){  }

  public logOut(){
    this.backend.logOut();
  }

  isExpanded: boolean = false
  state: string = 'initial'

  expand() {
    this.isExpanded = !this.isExpanded
    this.state = this.isExpanded ? 'expanded' : 'initial'
  }

  public get requests(): RequestEntity[] {
    if (this.storage.user == null){
        this.router.navigateByUrl("/login");
      return new Array(0);
    }
    return this.storage.user.requests;
  }
  
}
