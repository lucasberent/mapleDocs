import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import * as jwt_decode from 'jwt-decode';
import {Globals} from '../global/globals';
import {LoginDTO} from '../dto/login-dto';
import {RegisterDto} from '../dto/register-dto';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUrl: string = this.globals.backendUri + '/authenticate';
  private registerBaseUrl: string = this.globals.backendUri + '/register';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  authenticate(authRequest: LoginDTO): Observable<string> {
    return this.httpClient.post(this.authBaseUrl, authRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }

  isLoggedIn() {
    return !!this.getToken() && (this.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ROLE_ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'ROLE_USER';
      }
    }
    return 'UNDEFINED';
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private getTokenExpirationDate(token: string): Date {

    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  register(registerDTO: RegisterDto) {
    return this.httpClient.post(this.registerBaseUrl, registerDTO)
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }
}
