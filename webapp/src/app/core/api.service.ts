import {Injectable} from '@angular/core';
import {Observable, of, throwError} from 'rxjs';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpRequest} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {catchError, retry, tap} from 'rxjs/operators';
import {Team} from '../shared/model/Team';
import {LoginRequest, RegisterRequest, Model} from '../shared/model/Model';
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
  readonly api = 'http://localhost:8080/api/v1';
  readonly base = `${this.api}`;
  readonly v3 = 'v3'
  readonly githubbase = `https://api.github.com`;

  readonly API_BASE_URL = 'http://localhost:8080';
  readonly ACCESS_TOKEN = 'accessToken';

  readonly OAUTH2_REDIRECT_URI = 'http://localhost:4200/oauth2/redirect';

  readonly GOOGLE_AUTH_URL = this.API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + this.OAUTH2_REDIRECT_URI;
  readonly GITHUB_AUTH_URL = this.API_BASE_URL + '/oauth2/authorize/github?redirect_uri=' + this.OAUTH2_REDIRECT_URI;

  constructor(private http: HttpClient) {
  }

  static githubHeaderWithCredentials(): object {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Token ${window.sessionStorage.getItem('github_access_token')}`,
        'Accept': `application/vnd.github.machine-man-preview`
      })
    };
  }

  static backendHeaderWithCredentials(): object {
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${window.sessionStorage.getItem('access_token')}`,
      })
    };
  }

  getIssues(repo ): Observable<any> {
    const endpoint = `${this.githubbase}/repos/${repo}/issues`;
    return this.http.get(endpoint).pipe(
      tap(x => console.log(`fetched issues data`)),
      catchError(this.handleError('Issues', []))
    );
  }

  getRepoIssues(repo): Observable<any[]> {
    const endpoint = `${this.githubbase}/repos/${repo}/issues`;
    return this.http.get<any[]>(endpoint).pipe(
      tap(x => console.log(`fetched issues data`)),
      catchError(this.handleError('Issues', []))
    );
  }

  fetchAllRepositories(): Observable<any[]> {
    const endpoint = `${this.githubbase}/user/repos`;
    return this.http.get<any[]>(endpoint, ApiService.githubHeaderWithCredentials()).pipe(
      tap(x => console.log(`Lists repositories that the authenticated user has explicit permission`)),
      catchError(this.handleError('Issues', []))
    );
  }

  getRepositories(): Observable<any[]> {
    var repo = [];
    repo.push("anoited007/personalized-tab");
    return of(repo);
    const endpoint = `${this.base}/projects`;
    return this.http.get<any[]>(endpoint).pipe(
      tap(x => console.log(`fetched repositories data`)),
      catchError(this.handleError('ActivatedProjects', []))
    );
  }

  getAuthenticatedUserIssues(): Observable<any> {
    const endpoint = `${this.githubbase}/issues`;
    return this.http.get(endpoint,ApiService.githubHeaderWithCredentials()).pipe(
      tap(x => console.log(`fetched user assigned issues data`)),
      catchError(this.handleError('Issues', []))
    );
  }

  getUser(): Observable<any> {
    const endpoint = `${this.githubbase}/user`;
    return this.http.get(endpoint,ApiService.githubHeaderWithCredentials()).pipe(
      tap(x => console.log(`fetched user profile data`)),
      catchError(this.handleError('Issues', []))
    );
  }

  getUsersForLeaderbaord(): Observable<any> {
    const endpoint = `${this.base}/users`;
    return this.http.get(endpoint,ApiService.backendHeaderWithCredentials()).pipe(
      tap(x => console.log(`fetched user profile data`)),
      catchError(this.handleError('Issues', []))
    );
  }

  getUserProfile(email): Observable<any> {
    const endpoint = `${this.base}/user/points`;
    return this.http.get(endpoint,ApiService.backendHeaderWithCredentials()).pipe(
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

  setUserIssue(repositoryId): Observable<any> {
    const endpoint = `${this.base}/issue`;
    return this.http.post<any>(endpoint, repositoryId, ApiService.backendHeaderWithCredentials()).pipe(
      tap(x => console.log(`posted set issue for user`)),
      catchError(this.handleError('SetIssue', []))
    );
  }

  assignReward(reward): Observable<any> {
    const endpoint = `${this.base}/issue/incentive`;
      return this.http.post<any>(endpoint, reward, ApiService.backendHeaderWithCredentials()).pipe(
      tap(x => console.log(`posted issue incentive`)),
      catchError(this.handleError('PostIncentive', []))
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
    return this.http.post<any>(this.GITHUB_AUTH_URL,{}).pipe(
      tap(x => console.log(`posted login data`)),
      catchError(this.handleError('login', []))
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
