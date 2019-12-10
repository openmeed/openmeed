import {Component, OnInit, SecurityContext} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../core/authentication.service';
import {ApiService} from '../../core/api.service';
import {DomSanitizer} from '@angular/platform-browser';
import {LoginRequest, Model} from '../../shared/model/Model';

@Component({
  selector: 'openmeed-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  errorMessage;
  user = new LoginRequest();
  title;
  sso = true;

  constructor(private domSanitizer: DomSanitizer,
              private authService: AuthenticationService,
              private http: ApiService, private router: Router) {
  }

  ngOnInit() {
  }

  login() {
    if(this.sso){
      this.http.loginWithOauth().subscribe((response) => {
        console.log(response)
      });
      this.router.navigateByUrl("/dashboard")
      return;
    }
  }
}
