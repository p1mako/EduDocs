import { Injectable } from '@angular/core';
import { Observable, catchError, retry, throwError, of, map } from 'rxjs';
import { RequestEntity, StorageService, Template } from './storage.service';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';


@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private storage: StorageService, private auth: AuthService, private router: Router) { }

  private adress = "http://localhost:8080/";

  private authenticate(): Observable<boolean> {
    return this.auth.logIn();
  }

  logIn(login: string, password: string): Observable<boolean> {
    return this.auth.logIn(login, password)
  }

  logOut() {
    this.auth.logOut()
  }

  isLoggedIn(): Observable<boolean> {
    return this.authenticate();
  }

  getTemplates(): Observable<Template[]> {
    return this.auth.get<Template[]>(this.adress + BackendAdresses.getTemplates).pipe<Template[]>(
      catchError((err: { status_code: number, message: string }) => {
        if (err.status_code == 401) {
          this.auth.loggedIn.next(false)
          this.router.navigateByUrl("/login")
        }
        return []
      })
    )
  }

  getRequests(): Observable<RequestEntity[]>{
    return this.auth.get<RequestEntity[]>(this.adress + BackendAdresses.getRequests)
  }

  addTemplate(template: Template): Observable<Template[]> {
    return this.auth.post<Template[]>(this.adress + BackendAdresses.addTemplate, template)
  }
}

enum BackendAdresses {
  login = "login",
  changeUser = "user/update",
  createUser = "user/create",
  createRequest = "request/create",
  getRequests = "request/all",
  updateRequest = "request/update",
  getTemplates = "templates/get",
  addTemplate = "templates/add"
}
