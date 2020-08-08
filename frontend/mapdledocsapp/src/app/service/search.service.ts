import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable, of} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {MaDmpDto} from '../dto/madmp-dto';
import {ToastrService} from 'ngx-toastr';
import {SearchResponse} from '../dto/search-response';
import {SearchDTO} from "../dto/search-dto";

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  // MongoDB
  private searchBaseUrl: string = this.globals.backendUri + '/madmps';
  // Elasticsearch
  private searchUrl: string = this.globals.elasticsearchUri + '/madmps_nested/_search';

  constructor(private httpClient: HttpClient, private globals: Globals, private toastrService: ToastrService) {
  }

  buildAllFieldsCombinedQuery(searchDTO: SearchDTO, queries: any[]): void {
    this.addEthicalIssuesQuery(searchDTO, queries);
    this.addEmbargoQuery(searchDTO, queries);
    this.addCreationDateQuery(searchDTO, queries);
    this.addModificationDateQuery(searchDTO, queries);
    this.addContactPersonNameQuery(searchDTO, queries);
    this.addContactPersonEmailQuery(searchDTO, queries);
    this.addContactPersonIdentifier(searchDTO, queries);
    this.addContactPersonIdentifierTypeQuery(searchDTO, queries);
    this.addDatasetIdentifierQuery(searchDTO, queries);
    this.addDatasetIdentifierTypeQuery(searchDTO, queries);
    this.addDatasetDistributionHostTitle(searchDTO, queries);
    this.addMetadataStandardIdQuery(searchDTO, queries);
    this.addMetadataStandardIdTypeQuery(searchDTO, queries);
  }

  findMaDmpsCombined(searchDTO: SearchDTO): Observable<SearchResponse<any>> {
    let andQueries = [];
    console.log('combined search: ');
    console.log(searchDTO);

    this.buildAllFieldsCombinedQuery(searchDTO, andQueries);

    return this.httpClient.post<SearchResponse<any>>(this.searchUrl, {
      from: searchDTO.page * searchDTO.size,
      size: searchDTO.size,
      query: {
        bool: {
          must: andQueries
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
          tap(_ => {
            console.log('fetched madmp');
          }),
          catchError(err => this.handleError<any>('fetching madmps', err))
        );
    }
  }

  findMaDmps(searchString: string, page: number, size: number): Observable<SearchResponse<any>> {
    return this.httpClient.post<SearchResponse<any>>(this.searchUrl, {
      from: page * size,
      size: size,
      query: {
        multi_match: {
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

  findMaDmpsCustomField(field: string, searchString: string, page: number, size: number): Observable<SearchResponse<any>> {
    const searchField = 'dmp.' + field;
    return this.httpClient.post<SearchResponse<any>>(this.searchUrl, {
      from: page * size,
      size: size,
      query: {
        multi_match: {
          query: searchString,
          // fields: ['dmp_contact_contact_id_identifier', 'dmp_contact_contact_id_type',
          //   'dmp_contact_mbox', 'dmp_contact_name', 'dmp_description', 'dmp_id_identifier',
          //   'dmp_dmp_id_identifier', 'dmp_dmp_id_type', 'dmp_language', 'dmp_title'
          // ],
          fields: [searchField],
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

  addEthicalIssuesQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.ethicalIssues === 'yes' || searchDTO.ethicalIssues === 'no') {
      queries.push({
        term: {
          'dmp.ethical_issues_exist': searchDTO.ethicalIssues
        }
      });
    }
  }

  addEmbargoQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.embargo === 'yes' || searchDTO.embargo === 'no') {
      const embargoExistsQuery: any = {
        nested: {
          path: 'dmp.dataset',
          query: {
            nested: {
              path: 'dmp.dataset.distribution',
              query: {
                nested: {
                  path: 'dmp.dataset.distribution.license',
                  query: {
                    range: {
                      'dmp.dataset.distribution.license.start_date': {
                        gt: new Date()
                      }
                    }
                  }
                }
              }
            }
          }
        }
      };

      if (searchDTO.embargo === 'yes') {
        queries.push(embargoExistsQuery);
      } else {
        queries.push({
          bool: {
            must_not: embargoExistsQuery
          }
        });
      }
    }
  }

  addCreationDateQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.creationFromDate !== null || searchDTO.creationToDate !== null) {
      let conditions = {};
      if (searchDTO.creationFromDate !== null) {
        conditions['gte'] = searchDTO.creationFromDate;
      }
      if (searchDTO.creationToDate !== null) {
        conditions['lte'] = searchDTO.creationToDate;
      }
      queries.push({
        range: {
          'dmp.created': conditions
        }
      });
    }
  }

  addModificationDateQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.modificationFromDate !== null || searchDTO.modificationToDate !== null) {
      let conditions = {};
      if (searchDTO.modificationFromDate !== null) {
        conditions['gte'] = searchDTO.modificationFromDate;
      }
      if (searchDTO.modificationToDate !== null) {
        conditions['lte'] = searchDTO.modificationToDate;
      }
      queries.push({
        range: {
          'dmp.modified': conditions
        }
      });
    }
  }

  addContactPersonNameQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.contactPersonName) {
      console.log('adding name' + searchDTO.contactPersonName);
      queries.push({
        match_phrase: {
          'dmp.contact.name': searchDTO.contactPersonName
        }
      });
    }
  }

  addContactPersonEmailQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.contactPersonEmail) {
      queries.push({
        match: {
          'dmp.contact.mbox': searchDTO.contactPersonEmail
        }
      });
    }
  }

  addContactPersonIdentifier(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.contactPersonIdentifier) {
      queries.push({
        match: {
          'dmp.contact.contact_id.identifier': searchDTO.contactPersonIdentifier
        }
      });
    }
  }

  addContactPersonIdentifierTypeQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.contactPersonIdentifierType) {
      queries.push({
        match: {
          'dmp.contact.contact_id.type': searchDTO.contactPersonIdentifierType
        }
      });
    }
  }

  addDatasetIdentifierQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.datasetIdentifier) {
      queries.push({
        nested: {
          path: 'dmp.dataset',
          query: {
            bool: {
              must: [
                {
                  match: {
                    'dmp.dataset.dataset_id.identifier': searchDTO.datasetIdentifier
                  }
                }
              ]
            }
          }
        }
      });
    }
  }

  addDatasetIdentifierTypeQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.datasetIdentifierType) {
      queries.push({
        nested: {
          path: 'dmp.dataset',
          query: {
            bool: {
              must: [
                {
                  match: {
                    'dmp.dataset.dataset_id.type': searchDTO.datasetIdentifierType
                  }
                }
              ]
            }
          }
        }
      });
    }
  }

  addDatasetDistributionHostTitle(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.datasetDistributionHostTitle) {
      const matchDatasetHostTitle: any = {
        nested: {
          path: 'dmp.dataset',
          query: {
            nested: {
              path: 'dmp.dataset.distribution',
              query: {
                fuzzy: {
                  'dmp.dataset.distribution.host.title': {
                    value: searchDTO.datasetDistributionHostTitle,
                    fuzziness: 'AUTO',
                  }
                }
              }
            }
          }
        }
      };
      queries.push(matchDatasetHostTitle);
    }
  }

  addMetadataStandardIdQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.metadataStandardId) {
      const matchMetadataStandardIdQ: any = {
        nested: {
          path: 'dmp.dataset',
          query: {
            match: {
              'dmp.dataset.metadata.metadata_standard_id.identifier': searchDTO.metadataStandardId
            }
          }
        }
      };
      queries.push(matchMetadataStandardIdQ);
    }
  }

  addMetadataStandardIdTypeQuery(searchDTO: SearchDTO, queries: any[]): void {
    if (searchDTO.metadataStandardIdType) {
      const matchMetadataStandardIdTQ: any = {
        nested: {
          path: 'dmp.dataset',
          query: {
            match: {
              'dmp.dataset.metadata.metadata_standard_id.type': searchDTO.metadataStandardIdType
            }
          }
        }
      };
      queries.push(matchMetadataStandardIdTQ);
    }

  }
}
