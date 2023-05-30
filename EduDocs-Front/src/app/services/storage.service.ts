import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  currentUser: Student | Professor | Admin | null = null;

  constructor() { }

  public set user(currentUser : Student | Professor | Admin | null) {
    this.currentUser = currentUser;
  }

  
  public get user() : Student | Professor | Admin | null {
    return this.currentUser;
  }
  
}



export interface Entity{
  id: string | null;
}

export interface User extends Entity{
  login: string;
  password: string;
  name: string;
  surname: string;
  lastName: string | null;
}

export interface Template extends Entity{
  name: string;
  routeToDocument: string;
}

export interface Specialization extends Entity{
  name: string;
  registerNumber: string;
}

export interface Student extends User{
  availableRequests: RequestEntity[];
  entryDate: string;
  group: number;
  status: StudentStatus;
  uniqueNumber: number;
  specizlization: Specialization;
}

export interface Admin extends User{
  availableRequests: RequestEntity[];
  role: AdministrationRole;
  from: string;
  until: string;
  availableTemplates: [Template];
}

export interface Professor extends User{
  availableRequests: RequestEntity[];
  degree: string;
}

export interface Document extends Entity{
  template: Template;
  cerated: string;
  validThrough: string;
  author: Admin;
  inititator: User;
}

export interface RequestEntity extends Entity{
  status: RequestStatus;
  created: string;
  document: Document | null;
  template: Template;
  initiator: User;
}

export enum StudentStatus{
  Learning,
  AcademicVacation,
}

export enum RequestStatus {
  Sent = "green",
  BeingProcessed = "gray",
  CanBeTaken = "green",
  Received = "white",
  Declined = "red",
  Removed = "red"
}

export enum AdministrationRole {
  DEAN,
  EDUCATIONAL_DEPUTY,
  ACADEMIC_DEPUTY
}