import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { BackendService } from './services/backend.service';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('expandedPanel', [
      state('initial', style({ height: 0 })),
      state('expanded', style({ height: '*' })),
      transition('initial <=> expanded', animate('0.2s')),
    ]),
  ]
})
export class AppComponent {
  title = 'EduDocs-Front';
  isExpanded: boolean = false
  state: string = 'initial'

constructor(private back : BackendService, private router : Router, private auth: AuthService) {
  auth.loggedIn.subscribe((val) => {
    if (val) {
      this.btnText = "LOG OUT"
    } else {
      this.btnText = "LOG IN"
    }
  })
 }

  btnText : string = "LOG IN"

  expand() {
    this.isExpanded = !this.isExpanded
    this.state = this.isExpanded ? 'expanded' : 'initial'
  }

  requestsCallback(){
    this.router.navigateByUrl("/requests")
    this.expand()
  }

  logInCalback(){
    if (this.btnText == "LOG OUT"){
      this.back.logOut()
    }
    this.router.navigateByUrl("/login")
    this.expand()
  }
}
