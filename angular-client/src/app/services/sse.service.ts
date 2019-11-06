import { Observable, of, never } from 'rxjs';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable()
export class OrderSseService {
    sseUrl = 'http://localhost:8080/order-stream';
    constructor() {
    }

    orderStream(): Subject<any> {
        const subject = new Subject<any>();
        const eventSource = new EventSource(this.sseUrl);
        eventSource.addEventListener('order-stream-event', (evt: MessageEvent) => {
            const json = JSON.parse(evt.data);
            console.log(json);
            subject.next(json);
        });
        return subject;

    }
}
