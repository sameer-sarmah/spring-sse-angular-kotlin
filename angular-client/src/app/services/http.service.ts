import { Injectable } from '@angular/core';
import { Observable, of, never, throwError  } from 'rxjs';
import { HttpParams , HttpClient, HttpRequest, HttpResponse, HttpHeaders, HttpErrorResponse} from '@angular/common/http';
import { CoreException } from './core-exception';
import { KeyValue } from './KeyValueType';
import { map, filter, catchError } from 'rxjs/operators';


@Injectable()
export class HttpService {

  constructor( private httpClient: HttpClient) { }


  initiateRequest(method: string, url: string, params: KeyValue= {}, headers: KeyValue= {}, payload= null): Observable<any> {
    let queryParams = null;
    if (typeof params === 'object') {
      queryParams = new HttpParams({
        fromObject: params
      });
    }

    const httpHeaders = new HttpHeaders(headers);
    let request;
    if (method === 'GET' || method === 'DELETE') {
      request = new HttpRequest(method, url);
    } else {
      request = new HttpRequest(method, url, payload, {params: queryParams, headers: httpHeaders});
    }

    return this.httpClient.request(request)
    .pipe(filter((response: HttpResponse<any>) => {
      if (!response.body) {
       return false;
      } else {
        return true;
      }
     }),
     map(
      (response: HttpResponse<any>) => {
        let data = {};
        if (!!response.body && !!response.body.value) {
          data = response.body.value;
          return of(data);
        } else if (!!response.body) {
          data = response.body;
          return of(data);
        } else {
           return never();
        }
      }
    ),
    catchError(
    (error: HttpErrorResponse) => {
        const status = error.status;
        const statusText = error.statusText;
        const errorText = error.message;
        const exp = new CoreException(status, statusText, errorText);
        return throwError (exp);
      }
    ));


  }



}
