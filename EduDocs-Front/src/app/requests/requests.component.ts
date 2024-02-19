import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { RequestEntity, StorageService, Template } from '../services/storage.service';
import { Router } from '@angular/router';
import { BackendService } from '../services/backend.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.css', '../main/main.component.css'],
})
export class RequestsComponent {

  template: number = 1;

  constructor(protected storage: StorageService, private router: Router, private backend: BackendService, private auth: AuthService) {  }

  private loadData(){
    this.backend.getTemplates().subscribe({
      next: (templates) => this.storage.templates = templates
    })
    this.backend.getRequests().subscribe({
      next: (requests) => this.storage.requests = requests
    })
  }

  ngOnInit() {
    console.log("ngOnInit")
    this.loadData()
    this.auth.loggedIn.subscribe((loggedIn) => {
      console.log("Logged In: ", loggedIn)
      if (loggedIn) {
        this.loadData()
      }
    })
  }

  public editTemplates() {
    this.router.navigateByUrl("/templates")
  }

  addRequest() {

  }

}
