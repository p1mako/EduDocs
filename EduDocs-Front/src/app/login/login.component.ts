import { Component } from '@angular/core';
import { BackendService } from '../services/backend.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {

  constructor(private backend: BackendService, private router: Router) { }

  login = "";
  password = "";
  error = false;

  submit() : void{
    this.error = true;
    console.log(this.login + " " + this.password)
    this.backend.authenticate(this.login, this.password). subscribe({
      next : (x) => {
        this.error = false;
      },
      error : (err) =>  {
        this.error = true;
        console.error("error");
        
      },
      complete : () => {
        console.log('done');
        this.router.navigateByUrl("/requests");

      },
    });
  }
}
