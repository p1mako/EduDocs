import { Inject, NgModule, inject } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RequestComponent } from './request/request.component';
import { RequestsComponent } from './requests/requests.component';
import { MainComponent } from './main/main.component';
import { BackendService } from './services/backend.service';

var loginGuard = () => { 
  var back = inject(BackendService);
  return back.isLoggedIn();
};

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'login', component: LoginComponent },
  { path: 'request', component: RequestComponent/*, canActivate: [loginGuard] */},
  { path: 'requests', component: RequestsComponent/*, canActivate: [loginGuard] */}
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
