import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable, of} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {MaDmpDto} from '../dto/madmp-dto';
import {ToastrService} from 'ngx-toastr';
import {SearchResponse} from '../dto/search-response';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private searchBaseUrl: string = this.globals.backendUri + '/madmps';
  private searchUrl: string = this.globals.elasticsearchUri + '/madmps_private';

  constructor(private httpClient: HttpClient, private globals: Globals, private toastrService: ToastrService) {
  }

  findMaDmps(searchString: string, page: number, size: number): Observable<SearchResponse<any>> {
    return this.httpClient.post<SearchResponse<any>>(this.searchUrl + '/_search', {
      from: page,
      size: size,
      query: {
        multi_match : {
          query: searchString,
          // fields: ['dmp_contact_contact_id_identifier', 'dmp_contact_contact_id_type',
          //   'dmp_contact_mbox', 'dmp_contact_name', 'dmp_description', 'dmp_id_identifier',
          //   'dmp_dmp_id_identifier', 'dmp_dmp_id_type', 'dmp_language', 'dmp_title'
          // ],
          fields: ['*'],
          type: 'most_fields',
          fuzziness: 'AUTO'
        }
      }
    })
      .pipe(
        tap(list => {
          console.log('fetched madmps');
          console.log(list);
        }),
        catchError(this.handleError<any>('fetching madmps'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      this.toastrService.error('error while' + operation + ': ' + error.message);
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
