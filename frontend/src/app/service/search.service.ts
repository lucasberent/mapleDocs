import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable, of} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private searchBaseUrl: string = this.globals.backendUri + '/search';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  findMaDmps(searchString:string): Observable<any[]> {
    return this.httpClient.get<any[]>(this.searchBaseUrl)
      .pipe(
        tap(_ => console.log('fetched madmps')),
        catchError(this.handleError<any>('fetching madmps'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
