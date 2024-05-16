import { getLocaleId } from '@angular/common';
import { Injectable, InjectionToken, LOCALE_ID, inject } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private currentRole : Roles = Roles.Undefined
  private currentUser : User | undefined = undefined
  templates: Template[] = [];
  requests: Subject<RequestEntity[]> = new Subject<RequestEntity[]>()

  constructor() { }

  public get admin() : Admin | undefined {
    if (this.currentRole != Roles.Admin){
      return undefined
    }
    return this.currentUser as Admin
  }

  public set admin(admin: Admin | undefined ) {
    this.currentRole = Roles.Admin
    this.currentUser = admin
  }

  public get professor() : Professor | undefined {
    if (this.currentRole != Roles.Professor){
      return undefined
    }
    return this.currentUser as Professor
  }

  public set professor(professor: Professor | undefined ) {
    this.currentRole = Roles.Professor
    this.currentUser = professor
  }

  public get student() : Student | undefined {
    if (this.currentRole != Roles.Student){
      return undefined
    }
    return this.currentUser as Student
  }

  public set student(student: Student | undefined ) {
    this.currentRole = Roles.Student
    this.currentUser = student
  }

  public get user(): User | undefined {
    return this.currentUser;
  }

  clean() {
    this.templates = []
    this.requests.next([])
    this.currentUser = undefined
  }
}



export interface Entity {
  uuid: string | undefined;
}

export interface User extends Entity {
  login: string;
  password: string;
  name: string;
  surname: string;
  lastName: string | null;
}

export interface Template extends Entity {
  name: string;
  routeToDocument: string;
  responsibleAdmin: AdministrationRole
}

export interface Specialization extends Entity {
  name: string;
  registerNumber: string;
}

export interface Student extends User {
  entryDate: string;
  group: number;
  status: StudentStatus;
  uniqueNumber: number;
  specialization: Specialization;
}

export interface Admin extends User {
  role: AdministrationRole;
  from: string;
  until: string;
  availableTemplates: [Template];
}

export interface Professor extends User {
  degree: string;
}

export interface Document extends Entity {
  template: Template;
  created: string | undefined;
  validThrough: string;
  author: Admin;
  inititator: User;
}

export interface RequestEntity extends Entity {
  status: RequestStatus;
  created: string | null;
  document: Document | null;
  template: Template;
  initiator: User;
}

export enum StudentStatus {
  Learning,
  AcademicVacation,
}

export enum RequestStatus {
  Sent,
  BeingProcessed,
  CanBeTaken,
  Received,
  Declined,
  Removed
}

export enum AdministrationRole {
  Dean,
  EducationalDeputy,
  AcademicDeputy,
}

export class Locale {

  static getLocaleAdministartionRoles(): string[] {
    var locale = inject(LOCALE_ID)
    console.log(locale)
    if (getLocaleId(locale) == 'ru') {
      return ["Декан",
        "Зам. декана по учебной работе",
        "Зам. декана по воспитательной работе"]
    } else {
      return ["Dean",
        "Educational deputy",
        "Academic deputy"]
    }
  }

  static getLocaleRequestStatuses(): string[] {
    var locale = inject(LOCALE_ID)
    console.log(locale)
    if (getLocaleId(locale) == 'ru') {
      return ["Отправлено",
        "В обработке",
        "Готово к выдаче",
        "Выдано",
        "Отклонено",
        "Удалено",]
    } else {
      return ["Sent",
      "In process",
      "To be taken",
      "Received",
      "Declined",
      "Removed"]
    }
  }
}

export function getLocaleAdministartionRoles(): string[] {
  var locale = inject(LOCALE_ID)
  console.log(locale)
  if (getLocaleId(locale) == 'ru') {
    return ["Декан",
      "Зам. декана по учебной работе",
      "Зам. декана по воспитательной работе"]
  } else {
    return ["Dean",
      "Educational deputy",
      "Academic deputy"]
  }
}


export enum Roles {
  Undefined,
  Admin,
  Professor,
  Student
}