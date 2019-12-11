import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private router: Router) {

  }

  public isLoggedIn() {
    return window.localStorage.getItem('access_token') !== null;
  }

  public logout() {
    window.localStorage.removeItem('access_token');
  }

}
