import {Component, OnInit, SecurityContext} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../core/authentication.service';
import {ApiService} from '../../core/api.service';
import {DomSanitizer} from '@angular/platform-browser';
import {environment} from "../../../environments/environment";

@Component({
  selector: 'openmeed-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  errorMessage;
  title;
  sso = true;
  url = `${environment.api}/oauth2/authorize/github?redirect_uri=${environment.webapp}/oauth2/redirect`
  //url = `${environment.api}/oauth2/authorize/github?redirect_uri=${environment.webapp}/index.html`

  constructor() {
  }

  ngOnInit() {
  }

}
