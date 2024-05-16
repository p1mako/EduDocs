import { NgModule, inject } from '@angular/core';
import { Router, RouterModule, Routes, UrlTree } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RequestComponent } from './request/request.component';
import { RequestsComponent } from './requests/requests.component';
import { MainComponent } from './main/main.component';
import { BackendService } from './services/backend.service';
import { TemplatesComponent } from './templates/templates.component';
import { map, of } from 'rxjs';

var loginGuard = () => { 
  var back = inject(BackendService);
  var router : Router = inject(Router)
  return back.isLoggedIn().pipe(
    map((val) => {
      if (val == false) {
        return of([router.parseUrl("/login")])
      }
      return true
    })
  )
};

var loggedInGuard = () => {
  var back = inject(BackendService)
  var router : Router = inject(Router)
  return back.isLoggedIn().pipe(
    map((val) => {
      if (val == true) {
        console.log("redirecting to requests")
        return false
      }
      console.log("not logged in")
      return true
    })
  )
}

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'login', component: LoginComponent, canActivate: [loggedInGuard] },
  { path: 'request/:id', component: RequestComponent, canActivate: [loginGuard] },
  { path: 'requests', component: RequestsComponent, canActivate: [loginGuard]},
  { path: 'templates', component: TemplatesComponent, canActivate: [loginGuard]}
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
