import { Component, OnInit, Input, OnChanges , SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-address-tile',
  templateUrl: './address-tile.component.html',
  styleUrls: ['./address-tile.component.css']
})
export class AddressTileComponent implements OnInit, OnChanges {
  @Input() order: any;
  address: Address;

  constructor() {

   }

  ngOnInit() {
    const order = this.order;
    if (!!this.order) {
    this.address = order.address;
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    for (const order in changes) {
      if (changes.hasOwnProperty(order)) {
          const change = changes[order];
          const currentOrder = change.currentValue;
          if (!!currentOrder) {
            this.address = currentOrder.address;
          }
      }
    }
  }

}

class Address {
  address: string;
  city: string;
  state: string;
  zip: string;
  country: string;
  phone: string;

}
