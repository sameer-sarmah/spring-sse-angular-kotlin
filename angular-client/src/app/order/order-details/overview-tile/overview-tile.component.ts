import { Component, OnInit, Input, OnChanges , SimpleChanges } from '@angular/core';
import {formatDate} from './../../util';

@Component({
  selector: 'app-overview-tile',
  templateUrl: './overview-tile.component.html',
  styleUrls: ['./overview-tile.component.css']
})
export class OverviewTileComponent implements OnInit, OnChanges {
  @Input() order: any;
  overviewData: Overview = new Overview();

  constructor() {

   }

  ngOnInit() {
    if (!!this.order) {
    const orderItem = this.order;
    this.overviewData.customerName = `${orderItem.customer.firstName} ${orderItem.customer.lastName}`;
    this.overviewData.shippedDate = formatDate(orderItem.shippedDate);
    this.overviewData.orderDate = formatDate(orderItem.orderDate);
    this.overviewData.shippingFee =  orderItem.shippingFee;
  }
}

ngOnChanges(changes: SimpleChanges) {
  for (const order in changes) {
    if (changes.hasOwnProperty(order)) {
        const change = changes[order];
        const currentOrder = change.currentValue;
        if (!!currentOrder) {
          const orderItem = currentOrder;
          this.overviewData.customerName = `${orderItem.customer.firstName} ${orderItem.customer.lastName}`;
          this.overviewData.shippedDate = formatDate(orderItem.shippedDate);
          this.overviewData.orderDate = formatDate(orderItem.orderDate);
          this.overviewData.shippingFee =  orderItem.shippingFee;
        }
    }
  }
}

}

class Overview {
  customerName: string;
  shippedDate: string;
  orderDate: string;
  shippingFee: number;
}
