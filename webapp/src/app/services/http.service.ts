import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpRequest} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {LoginRequest, RegisterRequest, TwoFactor} from "../shared/User";
import {catchError, tap} from "rxjs/operators";
import {Router} from "@angular/router";
import {Issue} from "../shared/modal/Issue";

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  readonly api = 'api';
  readonly base = `${this.api}`;

  constructor(private http: HttpClient) {
  }

  static headerWithCredentials(): object {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'withCredentials': 'true',
        'Authorization': `Bearer ${window.sessionStorage.getItem('access_token')}`
      })
    };
  }

  getIssues(): Observable<any> {
    const endpoint = `${this.base}/endpoint`;
    return this.http.get(endpoint).pipe(
      tap(x => console.log(`posted registration data`)),
      catchError(this.handleError('Register', []))
    );
  }

  assignIssue(memberId,issue:Issue): Observable<any> {
    const endpoint = `${this.base}/auth/register`;
    return this.http.post<any>(endpoint, issue).pipe(
      tap(x => console.log(`posted registration data`)),
      catchError(this.handleError('Register', []))
    );
  }

  register(data:RegisterRequest): Observable<any> {
    const endpoint = `${this.base}/auth/register`;
    return this.http.post<any>(endpoint, data).pipe(
      tap(x => console.log(`posted registration data`)),
      catchError(this.handleError('Register', []))
    );
  }

  login(body:LoginRequest): Observable<any> {
    const endpoint = `${this.base}/auth/login`;
    return this.http.post<any>(endpoint, body).pipe(
      tap(x => console.log(`posted login data`)),
      catchError(this.handleError('login', []))
    );
  }

  verifyTwoFA(data): Observable<any> {
    const endpoint = `${this.base}/auth/code`;
    return this.http.post<any>(endpoint,data).pipe(
      catchError(this.handleError('2FA', status)),
    );
  }

  private handleError<T>(operation = 'operation', result?:T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error.message}`);
      if(operation === '2FA'){
        alert('Wrong 2FA');
      }
      return of(result);
    };
  }

}
