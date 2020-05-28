import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable, of} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {MaDmpDto} from "../dto/madmp-dto";
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private searchBaseUrl: string = this.globals.backendUri + '/madmps';

  constructor(private httpClient: HttpClient, private globals: Globals, private toastrService: ToastrService) {
  }

  findMaDmps(searchString: string, page: number, size: number): Observable<MaDmpDto[]> {
    return this.httpClient.get<any[]>(this.searchBaseUrl + '?page=1&size=10')
      .pipe(
        tap(list => {
          console.log('fetched madmps')
          console.log(list);
        }),
        catchError(this.handleError<any>('fetching madmps'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      this.toastrService.error('error while' + operation + ': ' +error.message);
      console.error(error); // log to console instead
      console.log(`${operation} failed: ${error.message}`);
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  getMaDmp(id: string): Observable<MaDmpDto> {
    {
      return this.httpClient.get<MaDmpDto>(this.searchBaseUrl + '/details/' + id)
        .pipe(
          tap(_ => {console.log('fetched madmp')}),
          catchError(err => this.handleError<any>('fetching madmps', err))
        );
    }
  }
}
