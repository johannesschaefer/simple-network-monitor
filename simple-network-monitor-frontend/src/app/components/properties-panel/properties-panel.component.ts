import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

export interface KV {
  key : string;
  value : string;
}

@Component({
  selector: 'snm-properties-panel',
  templateUrl: './properties-panel.component.html',
  styleUrls: ['./properties-panel.component.css']
})
export class PropertiesPanelComponent implements OnInit {
  _props : KV[] = [];

  @Output()
  propsChange = new EventEmitter<{ [index: string]: string }>();

  @Input()
  secret : boolean = false;

  constructor() { }

  ngOnInit() {
  }

  get props() {
    let ret : { [index: string]: string };
    this._props.forEach( x => ret[x.key] = x.value);
    return ret;
  }

  @Input()
  set props(p : { [index: string]: string }) {
    if(typeof p == 'undefined') {
      this._props = [];
      return;
    }
    Object.keys(p).forEach( k => this._props.push({key: k, value: p[k]}));
  }

  public add() {
    this._props.push({key: "", value: ""});
  }

  public remove(p : KV) {
    let i = this._props.indexOf(p);
    if (i < 0) {
      return;
    }
    this._props.splice(i, 1);
  }
}
