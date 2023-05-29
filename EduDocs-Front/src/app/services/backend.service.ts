import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { Admin, Professor, StorageService, Student, StudentStatus, User } from './storage.service';
import { Router, UrlTree } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http: HttpClient, private storage: StorageService, private router: Router) { }

  private adress = "http://localhost:8080/EduDocsAPI/";

  authenticate(login: string, password: string) : Observable<Student | Professor | Admin> {
    var postHttp = this.http.post<Student | Professor | Admin>(this.adress + BackendAdresses.login, {
      login: login,
      password: password
    });

    postHttp.subscribe((user: Student | Professor | Admin) => {
      this.storage.user = user;
    })
    return postHttp;
  }

  logOut(){
    this.http.get(this.adress + BackendAdresses.logout, {withCredentials: true})
  }

  isLoggedIn() : Observable<boolean | UrlTree>{
    return new Observable<boolean | UrlTree>(
      (subscriber) => {
        this.http.get<Student | Professor | Admin>(this.adress + BackendAdresses.login, {withCredentials: true, responseType: "json"}).subscribe({
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
}

enum BackendAdresses {
  login = "login",
  logout = "logout",
  changeUser = "user/update",
  createUser = "user/create",
  createRequest = "request/create",
  getRequests = "request/all",
  updateRequest = "request/update"
}