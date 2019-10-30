import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor() {
  }

  public isLoggedIn() {
    return true;
   // return window.sessionStorage.getItem('access_token') !== null;
  }

  public logout() {
    window.sessionStorage.removeItem('access_token')
  }

}
