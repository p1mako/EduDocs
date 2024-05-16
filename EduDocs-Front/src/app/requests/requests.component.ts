import { Component } from '@angular/core';
import { RequestEntity, RequestStatus, StorageService, Template } from '../services/storage.service';
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
  requests: RequestEntity[] = []

  constructor(protected storage: StorageService, private router: Router, private backend: BackendService, private auth: AuthService) { }

  private loadData() {
    this.backend.getTemplates().subscribe({
      next: (templates) => this.storage.templates = templates
    })
    this.backend.getRequests();
  }

  ngOnInit() {
    console.log("ngOnInit")
    this.storage.requests.subscribe({
      next: (requests: RequestEntity[]) => {
        this.requests = requests
      }
    })
    //TODO:Make templates also a subject
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
    console.log("lslsls")
    console.log({ uuid: undefined, created: null, document: null, initiator: this.storage.user!, status: RequestStatus.Sent, template: this.storage.templates[this.template] })
    this.backend.addRequest({ uuid: undefined, created: null, document: null, initiator: this.storage.user!, status: RequestStatus.Sent, template: this.storage.templates[this.template] })
  }

  protected identifyRequest(index: number, request: RequestEntity) {
    return request.uuid
  }
}
