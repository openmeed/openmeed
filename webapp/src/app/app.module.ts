import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {LoginComponent} from "./pages/login/login.component";
import {ProfileComponent} from "./pages/profile/profile.component";
import {AdminComponent} from "./pages/admin/admin.component";
import {NotFoundComponent} from "./pages/not-found/not-found.component";
import {NavbarComponent} from "./shared/navbar/navbar.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ModalModule, TooltipModule} from "ngx-bootstrap";
import {CommonModule} from "@angular/common";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {RedirectComponent} from './pages/redirect/redirect.component';
import { BoardComponent } from './pages/board/board.component';
import {FooterComponent} from "./shared/footer/footer.component";
import {Interceptor} from "./core/Interceptor";

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    LoginComponent,
    ProfileComponent,
    AdminComponent,
    NotFoundComponent,
    NavbarComponent,
    RedirectComponent,
    BoardComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    CommonModule,
    TooltipModule.forRoot(),
    AppRoutingModule,
    ModalModule.forRoot(),
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: Interceptor, multi: true }  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
