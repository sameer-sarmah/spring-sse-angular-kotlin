import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Observable, of, never } from 'rxjs';

@Injectable()
export class OrderHttpService {
  private svcURL = 'http://localhost:8080/order';

  constructor(private httpService: HttpService) { }

  getOrders(headers: {}= {}): Observable<any> {
    const params = { };
    return this.httpService.initiateRequest('GET', this.svcURL, params, headers);

  }


  getOrder(id: number): Observable<any> {
    return this.httpService.initiateRequest('GET', `${this.svcURL}/${id}` );
  }
}
