import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable, of} from 'rxjs';
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

  uploadMaDmp(maDmp: CreateMaDmpDto): Observable<any> { // TODO fix
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    console.log('posting madmp in service');
    return this.httpClient.post(this.maDmpBaseUrl, maDmp, httpOptions);
  }
}
