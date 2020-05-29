import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';
import {CreateMaDmpDto} from '../dto/create-madmp-dto';
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  private maDmpBaseUrl: string = this.globals.backendUri + '/madmps';

  constructor(private httpClient: HttpClient, private globals: Globals, private toastrService: ToastrService) {
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      this.toastrService.error('error while' + operation + ': ' +error.message)
      console.error(error); // log to console instead
      console.log(`${operation} failed: ${error.message}`);
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  uploadMaDmp(maDmp: CreateMaDmpDto): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'}),
      responseType: 'text' as 'json'
    };
    console.log('posting madmp in service');
    return this.httpClient.post<string>(this.maDmpBaseUrl, maDmp, httpOptions)
      .pipe(
      tap(_ => {console.log('uploaded madmp')}),
      catchError(err => this.handleError('upload madmp', err))
    );
  }
}
