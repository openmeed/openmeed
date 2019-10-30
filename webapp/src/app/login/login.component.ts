import {Component, OnInit, SecurityContext} from '@angular/core';
import {HttpService} from "../services/http.service";
import {AuthenticationService} from "../services/authentication.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoginRequest, TwoFactor} from "../shared/User";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errorMessage;
  loginForm: FormGroup;
  isSubmitted = false;
  twoFA = false;
  code = new TwoFactor();
  user = new LoginRequest();
  title = "Sign In"

  constructor(private domSanitizer: DomSanitizer, private authService: AuthenticationService, private http: HttpService, private router: Router, private formBuilder: FormBuilder) {
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

  register() {
    this.router.navigate(['/register']);
  }

  verifyTFA() {
    this.http.verifyTwoFA(this.code).subscribe((res) => {
      if (res === true) {
        this.router.navigate(['/dashboard']);
      } else if (res == 401) {
        window.location.reload();
        window.sessionStorage.removeItem('access_token');

      }
    })
  }

  set2FA(event) {
    this.code.code = event.target.value;
    this.code.username = this.user.usernameOrEmail
  }

  login() {
    this.isSubmitted = true;
    if (this.loginForm.invalid) {
      this.errorMessage = 'Failed';
      return;
    }
    this.user.usernameOrEmail = this.domSanitizer.sanitize(SecurityContext.HTML, this.loginForm.controls['username'].value);
    this.user.password = this.domSanitizer.sanitize(SecurityContext.HTML, this.loginForm.controls['password'].value);
    this.user.twoFA = true;
    this.http.login(this.user).subscribe((response) => {
      if (response.length == 0) {
        alert('Invalid Credentials');
        return;
      } else {
        window.sessionStorage.setItem('access_token', response.accessToken);
        this.twoFA = true;
        this.title = 'Enter 2FA Code (Check Email)'
      }
    });
  }
}
