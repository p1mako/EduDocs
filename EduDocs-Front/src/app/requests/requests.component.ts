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
  template: number = 1;

  constructor(public storage: StorageService, private router: Router, private backend: BackendService) {
    //this.requests = storage.user?.availableRequests!;
    //TODO: seperate request for list of requests
  }

  ngOnInit() {
    console.log(this.storage.user)
    //this.requests = this.storage.user?.availableRequests!;
    //TODO: seperate request for list of requests
    console.log(this.requests);
    this.backend.getTemplates();

  }

  public editTemplates() {
    this.router.navigateByUrl("/templates")
  }

  addRequest() {

  }

}
