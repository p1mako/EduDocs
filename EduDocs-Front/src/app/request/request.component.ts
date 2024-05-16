import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RequestEntity, StorageService, Locale } from '../services/storage.service';
import { BackendService } from '../services/backend.service';

@Component({
  selector: 'app-request',
  templateUrl: './request.component.html',
  styleUrls: ['./request.component.css']
})

export class RequestComponent {

  protected status: string = "";
  protected document: string = "";
  protected validThrough: string = "";
  protected statuses: string[] = Locale.getLocaleRequestStatuses()
  protected request: RequestEntity | undefined

  private id: string = ""

  private requests: RequestEntity[] = []
  constructor(private activatedRoute: ActivatedRoute, private storage: StorageService, private backend : BackendService) {
    this.storage.requests.subscribe((requests: RequestEntity[]) => {
      this.requests = requests
      this.activatedRoute.params.subscribe(params => {
        this.id = params['id'];
        for (var i = 0; i < this.requests.length; i++) {
          if (this.requests[i].uuid == this.id) {
            this.request = this.requests[i]
          }
        }
      })
    })
    
    
  }

  onRequestChange() {
    this.request!.status = this.statuses.indexOf(this.status)
    this.request!.document = {
      uuid: undefined,
      created: undefined,
      inititator: this.request!.initiator,
      template: this.request!.template,
      validThrough: this.validThrough,
      author: this.storage.admin!
    }
  }
  submit() {
    this.request!.status = this.statuses.indexOf(this.status)
    this.request!.document = {
      uuid: undefined,
      created: undefined,
      inititator: this.request!.initiator,
      template: this.request!.template,
      validThrough: this.validThrough,
      author: this.storage.admin!
    }
    this.backend.updateRequest(this.request!)
  }
}
