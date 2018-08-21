import { Component, OnInit, Input } from '@angular/core';
import { Host } from '../../entities/host';

@Component({
  selector: 'snm-sensor-list',
  templateUrl: './sensor-list.component.html',
  styleUrls: ['./sensor-list.component.css']
})
export class SensorListComponent implements OnInit {
  @Input()
  host : Host;
  
  constructor() { }

  ngOnInit() {
  }

  autoDiscover() {
    console.info('autoDiscover'); // TODO
  }

  add() {
    console.info('add'); // TODO
  }

}
