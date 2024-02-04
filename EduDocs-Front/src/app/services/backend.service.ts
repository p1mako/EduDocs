import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { Admin, Professor, StorageService, Student, StudentStatus, Template, User } from './storage.service';
import { Router, UrlTree } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http: HttpClient, private storage: StorageService, private router: Router) { }

  private adress = "http://localhost:8080/";

  authenticate(login: string, password: string): Observable<Student | Professor | Admin> {
    var postHttp = this.http.post<Student | Professor | Admin>(this.adress + BackendAdresses.login, {
      login: login,
      password: password
    });
    return postHttp;
  }

  logOut() {
    this.http.get<null>(this.adress + BackendAdresses.logout).subscribe(() => { this.router.navigateByUrl("/login") });
  }

  isLoggedIn(): Observable<boolean | UrlTree> {
    return new Observable<boolean | UrlTree>(
      (subscriber) => {
        this.http.get<Student | Professor | Admin>(this.adress + BackendAdresses.login, { responseType: "json" }).subscribe({
          complete: () => {
            subscriber.next(true);
            subscriber.complete;
          },
          error: () => {
            subscriber.next(this.router.parseUrl("/login"));
            subscriber.complete();
          }
        })
      }
    );
  }

  getTemplates() {
    this.http.get<Template[]>(this.adress + BackendAdresses.getTemplates).subscribe((templates) => {
      this.storage.templates = templates;
    })
  }
}

enum BackendAdresses {
  login = "login",
  logout = "logout",
  changeUser = "user/update",
  createUser = "user/create",
  createRequest = "request/create",
  getRequests = "request/all",
  updateRequest = "request/update",
  getTemplates = "templates"
}
