import { getLocaleId } from '@angular/common';
import { Injectable, InjectionToken, LOCALE_ID, inject } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  currentUser: Student | Professor | Admin | undefined = undefined;
  templates: Template[] = [];
  requests: Subject<RequestEntity[]> = new Subject<RequestEntity[]>()

  constructor() { }

  public set user(currentUser: Student | Professor | Admin | undefined) {
    this.currentUser = currentUser;
  }


  public get user(): Student | Professor | Admin | undefined {
    return this.currentUser;
  }

  clean() {
    this.templates = []
    this.requests.next([])
    this.currentUser = undefined
  }
}



export interface Entity {
  id: string | undefined;
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
  created: string;
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
