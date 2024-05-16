import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, assertPlatform } from '@angular/core';
import { Buffer } from "buffer";
import { Observable, Subject, asyncScheduler, catchError, map, of, scheduled } from 'rxjs';
import { Admin, Professor, StorageService, Student } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private storage: StorageService) { }

  loggedIn = new Subject<boolean>()

  private login: string | null = null
  private password: string | null = null

  private authUser(user: string, password: string): Observable<boolean> {
    var headers = { headers: this.getAuthHeaders(user, password) }
    return new Observable<boolean>((subscriber) => {
      this.http.get("http://localhost:8080/login",
        headers).subscribe({
          error: () => {
            subscriber.next(false)
            this.loggedIn.next(false)
          },
          next: (val) => {
            if ((val as Admin).from) {
              this.storage.admin = val as Admin
            } else if (val as Professor) {
              this.storage.professor = val as Professor
            } else if (val as Student) {
              this.storage.student = val as Student
            }
            this.loggedIn.next(true)
            subscriber.next(true)
          },
          complete: () => subscriber.complete()
        })
    })
  }

  public logIn(login = sessionStorage.getItem("user"), password = sessionStorage.getItem("password")): Observable<boolean> {
    if (login == null || password == null) {
      this.loggedIn.next(false)
      return new Observable<boolean>((subscriber) => {
        subscriber.next(false)
      })
    }
    return this.authUser(login, password).pipe(
      map(
        (val, num) => {
          if (val) {
            sessionStorage.setItem("user", login)
            sessionStorage.setItem("password", password)
            this.login = login
            this.password = password
          } else {
            sessionStorage.setItem("user", login)
            sessionStorage.setItem("password", password)
            this.login = login
            this.password = password
          }
          return val
        }
      )
    )
  }

  public logOut() {
    sessionStorage.removeItem("user")
    sessionStorage.removeItem("password")
    this.login = ""
    this.password = ""
    this.storage.clean()
    this.loggedIn.next(false)
  }

  public post<T>(url: string, body: any): Observable<T> {
    return this.http.post<T>(url, body, { headers: this.getAuthHeaders() })
  }

  public get<T>(url: string): Observable<T> {
    return this.http.get<T>(url, { headers: this.getAuthHeaders() })
  }

  private getAuthHeaders(user = this.login, password = this.password) {
    return new HttpHeaders(
      {
        'Content-Type': 'application/json; charset=utf-8',
        'Authorization': 'Basic ' + Buffer.from(user + ':' + password).toString('base64')
      });
  }
}
