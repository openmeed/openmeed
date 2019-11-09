import {Injectable} from '@angular/core';
import {Observable, of, throwError} from 'rxjs';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpRequest} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {catchError, retry, tap} from 'rxjs/operators';
import {Team} from '../shared/model/Team';
import {LoginRequest, RegisterRequest, User} from '../shared/model/User';
import {Router} from '@angular/router';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};


@Injectable({
  providedIn: 'root'
})
export class ApiService {
  readonly api = 'api';
  readonly base = `${this.api}`;
  readonly v3 = 'v3'
  readonly githubbase = `https://api.github.com`;

  constructor(private http: HttpClient) {
  }

  static headerWithCredentials(): object {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Token ${window.sessionStorage.getItem('access_token')}`,
        'Accept': `application/vnd.github.machine-man-preview`
      })
    };
  }

  getIssues(owner,repo ): Observable<any> {
    const endpoint = `${this.githubbase}/repos/${owner}/${repo}/issues`;
    return this.http.get(endpoint).pipe(
      tap(x => console.log(`fetched issues data`)),
      catchError(this.handleError('Issues', []))
    );
  }

  getAuthenticatedUserIssues(): Observable<any> {
    const endpoint = `${this.githubbase}/issues`;
    return this.http.get(endpoint,ApiService.headerWithCredentials()).pipe(
      tap(x => console.log(`fetched user assigned issues data`)),
      catchError(this.handleError('Issues', []))
    );
  }

  getUser(): Observable<any> {
    const endpoint = `${this.githubbase}/user`;
    return this.http.get(endpoint,ApiService.headerWithCredentials()).pipe(
      tap(x => console.log(`fetched user profile data`)),
      catchError(this.handleError('Issues', []))
    );
  }

  register(data: RegisterRequest): Observable<any> {
    const endpoint = `${this.base}/auth/register`;
    return this.http.post<any>(endpoint, data).pipe(
      tap(x => console.log(`posted registration data`)),
      catchError(this.handleError('Register', []))
    );
  }

  login(body: LoginRequest): Observable<any> {
    const endpoint = `${this.base}/auth/login`;
    return this.http.post<any>(endpoint, body).pipe(
      tap(x => console.log(`posted login data`)),
      catchError(this.handleError('login', []))
    );
  }

  loginWithOauth(): Observable<any> {
    const endpoint = `http://localhost:8080/api/v1/auth/login`;
    return this.http.post<any>(endpoint,{}).pipe(
      tap(x => console.log(`posted login data`)),
      catchError(this.handleError('login', []))
    );
  }

  verifyTwoFA(data): Observable<any> {
    const endpoint = `${this.base}/auth/code`;
    return this.http.post<any>(endpoint, data).pipe(
      catchError(this.handleError('2FA', status)),
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error.message}`);
      if (operation === '2FA') {
        alert('Wrong 2FA');
      }
      return of(result);
    };
  }
}
