import { Component } from '@angular/core';
import { BackendService } from '../services/backend.service';
import { Router } from '@angular/router';
import { StorageService } from '../services/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {

  constructor(private backend: BackendService, private router: Router, private storage: StorageService) { }

  login = "";
  password = "";
  error = false;

  submit() : void{
    this.backend.logIn(this.login, this.password).subscribe({
      next : (val) => {
        this.error = val;
        if (val) {
          this.router.navigateByUrl("/requests")
        }
      },
      error : (err) =>  {
        this.error = true;
      },
      complete : () => {
        this.router.navigateByUrl("/requests");
      },
    });
  }
}
