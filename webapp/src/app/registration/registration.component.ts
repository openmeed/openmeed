import {Component, OnInit, SecurityContext} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../services/authentication.service";
import {HttpService} from "../services/http.service";
import {Router} from "@angular/router";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";
import {RegisterRequest} from "../shared/User";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  errorMessage;
  error = false;
  registrationForm: FormGroup;
  isSubmitted  =  false;
  user = new RegisterRequest();
  constructor(private sanitizer: DomSanitizer, private authService: AuthenticationService, private http :HttpService,private router: Router, private formBuilder: FormBuilder ) { }

  ngOnInit() {
    this.registrationForm  =  this.formBuilder.group({
      name: ['', [Validators.required]],
      career: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      company: ['', Validators.required],
      username: ['', [Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
      password: ['', [Validators.required, Validators.minLength(8),Validators.maxLength(50)]]
    });
  }

  register(){
    this.isSubmitted = true;
    if(this.registrationForm.invalid){
      this.error = true;
      this.errorMessage = 'Failed';
      if(!this.validatePassword(this.registrationForm.controls['password'].value)) {
        this.errorMessage = 'Enter Strong password. Password does not conform to password policy'
      }
      return false;
    }

    this.user.name = this.sanitizer.sanitize(SecurityContext.HTML, this.registrationForm.controls['name'].value);
    this.user.username = this.sanitizer.sanitize(SecurityContext.HTML, this.registrationForm.controls['username'].value);
    this.user.email = this.sanitizer.sanitize(SecurityContext.HTML, this.registrationForm.controls['email'].value);
    this.user.career = this.sanitizer.sanitize(SecurityContext.HTML, this.registrationForm.controls['career'].value);
    this.user.company = this.sanitizer.sanitize(SecurityContext.HTML, this.registrationForm.controls['company'].value);
    this.user.password = this.sanitizer.sanitize(SecurityContext.HTML, this.registrationForm.controls['password'].value);

    this.http.register(this.user).subscribe((response)=>{
      this.router.navigate(['/login'])
    });
  }

  get formControls() { return this.registrationForm.controls; }

  validatePassword(password: string): boolean {
    var specialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;
    var alphaNumeric = /[a-zA-Z0-9]/
    return ((specialChar.test(password)&& alphaNumeric.test(password)) && (password.length > 8) && (password.length < 40));
  }
}
