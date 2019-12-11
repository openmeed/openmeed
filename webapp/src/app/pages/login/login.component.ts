import {Component, OnInit, SecurityContext} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
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

  constructor(private router: Router, private activatedRoutes: ActivatedRoute) {
  }

  ngOnInit() {
    alert("we are in");
    console.log(history.state);
    const token = this.activatedRoutes.snapshot.queryParamMap.get("token");
    const access_token = this.activatedRoutes.snapshot.queryParamMap.get("access_token");
    const roles = this.activatedRoutes.snapshot.queryParamMap.get("roles");
    const username = this.activatedRoutes.snapshot.queryParamMap.get("username");
    console.log(token,access_token,roles,username)
    if (access_token != null) {
      localStorage.setItem('github_access_token', access_token);
      localStorage.setItem('access_token', token);
      localStorage.setItem('roles', roles);
      localStorage.setItem('username', username);
      this.router.navigateByUrl('/dashboard')
    } else {
      this.router.navigateByUrl('/login')
    }
  }

}
