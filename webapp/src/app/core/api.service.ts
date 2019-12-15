import {Injectable} from '@angular/core';
import {Observable, of, throwError} from 'rxjs';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams, HttpRequest} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {catchError, retry, tap} from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  readonly api = `${environment.api}/api/v1`;
  readonly base = `${this.api}`;
  readonly githubbase = `https://api.github.com`;

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

  fetchAllRepositories(): Observable<any[]> {
    const endpoint = `${this.githubbase}/user/repos?direction=desc`;
    return this.http.get<any[]>(endpoint, ApiService.githubHeaderWithCredentials()).pipe(
      tap(x => console.log(`Lists repositories that the authenticated user has explicit permission`)),
      catchError(this.handleError('Issues', []))
    );
  }

  getRepositories(): Observable<any[]> {
    const endpoint = `${this.base}/projects`;
    return this.http.get<any[]>(endpoint, ApiService.backendHeaderWithCredentials()).pipe(
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
  getIssuesIncentives(): Observable<any[]> {
    const endpoint = `${this.base}/issues`;
    return this.http.get<any[]>(endpoint,ApiService.backendHeaderWithCredentials()).pipe(
      tap(x => console.log(`fetched issues incentive data`)),
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
    const endpoint = `${this.base}/user`;
    return this.http.get(endpoint,ApiService.backendHeaderWithCredentials()).pipe(
      tap(x => console.log(`fetched user profile data`)),
      catchError(this.handleError('Issues', []))
    );
  }

  setUserIssue(repositoryId): Observable<any> {
    const endpoint = `${this.base}/issue`;
    return this.http.post<any>(endpoint, repositoryId, ApiService.backendHeaderWithCredentials()).pipe(
      tap(x => console.log(`posted set issue for user`)),
      catchError(this.handleError('SetIssue', []))
    );
  }

  storeActivatedRepository(activatedRepositories): Observable<any> {
    const endpoint = `${this.base}/projects`;
    return this.http.post<any>(endpoint, activatedRepositories, ApiService.backendHeaderWithCredentials()).pipe(
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

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error.message}`);
      return of(result);
    };
  }
}
