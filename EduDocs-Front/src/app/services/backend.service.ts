import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, retry, throwError, of } from 'rxjs';
import { Admin, Professor, StorageService, Student, StudentStatus, Template, User } from './storage.service';
import { Router, UrlTree } from '@angular/router';
import { Buffer } from "buffer";


@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http: HttpClient, private storage: StorageService, private router: Router) { }

  private adress = "http://localhost:8080/";

  private login = ""
  private password = ""

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders(
      {
        'Content-Type': 'application/json; charset=utf-8',
        'Authorization': 'Basic ' + Buffer.from(this.login + ':' + this.password).toString('base64')
      });
  }

  private authenticate(): Observable<Student | Professor | Admin> {
    const headers = new HttpHeaders(
      {
        'Content-Type': 'application/json; charset=utf-8',
        'Authorization': 'Basic ' + Buffer.from(this.login + ':' + this.password).toString('base64')
      }
    )
    return this.http.get<Student | Professor | Admin>(this.adress + BackendAdresses.login, { headers: this.getAuthHeaders() });
  }



  logIn(login: string, password: string): Observable<Student | Professor | Admin> {
    this.login = login
    this.password = password;
    return this.authenticate();
  }

  logOut() {
    this.login = ""
    this.password = ""
  }

  isLoggedIn(): Observable<boolean | UrlTree> {
    return new Observable<boolean | UrlTree>(
      (subscriber) => {
        this.authenticate().subscribe({
          complete: () => {
            subscriber.next(true);
            subscriber.complete();
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
    this.http.get<Template[]>(this.adress + BackendAdresses.getTemplates, { headers: this.getAuthHeaders() }).pipe(
      catchError((err: { status_code: number, message: string }) => {
        if (err.status_code == 401) {
          this.router.navigateByUrl("/login")
        }
        return of([])
      })
    ).subscribe({
      next: templates => {
        this.storage.templates = templates
      },
    })
  }
}

enum BackendAdresses {
  login = "login",
  changeUser = "user/update",
  createUser = "user/create",
  createRequest = "request/create",
  getRequests = "request/all",
  updateRequest = "request/update",
  getTemplates = "templates"
}
