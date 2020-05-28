import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable, of} from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import {CreateMaDmpDto} from '../dto/create-madmp-dto';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  private maDmpBaseUrl: string = this.globals.backendUri + '/madmps';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead
      console.log(`${operation} failed: ${error.message}`);
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  uploadMaDmp(maDmp: CreateMaDmpDto): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      responseType: 'text'
    };
    console.log('posting madmp in service');
    return this.httpClient.post<string>(this.maDmpBaseUrl, maDmp, httpOptions);
  }
}
