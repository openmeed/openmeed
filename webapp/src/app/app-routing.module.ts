import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {NotFoundComponent} from "./not-found/not-found.component";
import {AdminComponent} from "./admin/admin.component";
import {AuthGuard} from "./services/auth.guard";
import {RegistrationComponent} from "./registration/registration.component";
import {UnauthorizedComponent} from "./shared/unauthorized/unauthorized.component";

const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'login', component: LoginComponent },
  {path: 'register', component: RegistrationComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  {path: 'admin', component: AdminComponent, canActivate: [AuthGuard] },
  {path: 'unauthorized', component: UnauthorizedComponent },
  {path: '**', component: NotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
