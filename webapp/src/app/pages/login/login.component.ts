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
  twoFA = false;
  loginForm: FormGroup;
  isSubmitted = false;
  user = new LoginRequest();
  code;
  title;
  sso = true;

  constructor(private domSanitizer: DomSanitizer,
              private authService: AuthenticationService,
              private http: ApiService, private router: Router,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  get formControls() {
    return this.loginForm.controls;
  }

  set2FA(event) {
    this.code.code = event.target.value;
    this.code.username = this.user.usernameOrEmail;
  }

  login() {
    if(this.sso){
      this.http.loginWithOauth().subscribe((response) => {
        console.log(response)
      });
      this.router.navigateByUrl("/dashboard")
      return;
    }

    this.isSubmitted = true;
    if (this.loginForm.invalid) {
      this.errorMessage = 'Failed';
      return;
    }
    this.user.usernameOrEmail = this.domSanitizer.sanitize(SecurityContext.HTML, this.loginForm.controls['username'].value);
    this.user.password = this.domSanitizer.sanitize(SecurityContext.HTML, this.loginForm.controls['password'].value);
    this.user.twoFA = true;

    this.http.login(this.user).subscribe((response) => {
      if (response.length === 0) {
        alert('Invalid Credentials');
        return;
      } else {
        window.sessionStorage.setItem('access_token', response.accessToken);
        this.router.navigate(['/teams']);
        // this.twoFA = true;
      //  this.title = 'Enter 2FA Code (Check Email)';
      }
    });
  }
}
