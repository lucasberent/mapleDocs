import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private searchBaseUrl: string = this.globals.backendUri + '/search';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

}
