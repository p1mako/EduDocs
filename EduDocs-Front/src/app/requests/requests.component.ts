import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { RequestEntity, StorageService, Template } from '../services/storage.service';
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

  requests: RequestEntity[] = []; 
  templates: Template[] = [];

  constructor(public storage: StorageService, private router: Router, private backend: BackendService){ 
    this.requests = storage.user?.availableRequests!;
   }

  ngOnInit() {
    console.log(this.storage.user)
    this.requests = this.storage.user?.availableRequests!;
    console.log(this.requests);
  }

  

  public logOut(){
    this.backend.logOut();
  }

  isCreateOpened = false;
  stateCreate = 'initial';

  openCreate(){
    this.backend.getTemplates().add(() => {
      this.templates = this.storage.templates
    });
    this.isCreateOpened = !this.isCreateOpened
    this.stateCreate = this.stateCreate ? 'expanded' : 'initial'
  }
  

  isExpanded: boolean = false
  state: string = 'initial'

  expand() {
    this.isExpanded = !this.isExpanded
    this.state = this.isExpanded ? 'expanded' : 'initial'
  }
  
  addRequest(){

  }

}
