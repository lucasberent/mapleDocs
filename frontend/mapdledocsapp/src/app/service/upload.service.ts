import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable, of, throwError} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {CreateMaDmpDto} from '../dto/create-madmp-dto';
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  private maDmpBaseUrl: string = this.globals.backendUri + '/madmps';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  uploadMaDmp(maDmp: CreateMaDmpDto): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'}),
      responseType: 'text' as 'json'
    };
    console.log('posting madmp in service');
    return this.httpClient.post<string>(this.maDmpBaseUrl, maDmp, httpOptions);
  }
}
