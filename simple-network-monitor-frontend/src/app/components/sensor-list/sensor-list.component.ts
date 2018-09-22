import { Component, OnInit, Input, ViewChild, TemplateRef, Output, EventEmitter } from '@angular/core';
import { Host } from '../../entities/host';
import { Sensor } from '../../entities/sensor';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Command } from '../../entities/command';
import { CommandService } from '../../services/command.service';
import { SensorService } from '../../services/sensor.service';

@Component({
  selector: 'snm-sensor-list',
  templateUrl: './sensor-list.component.html',
  styleUrls: ['./sensor-list.component.css']
})
export class SensorListComponent implements OnInit {
  @Input()
  host : Host;

  @Output()
  sensorChanged = new EventEmitter<boolean>();
  
  constructor(private commandService : CommandService, private sensorService : SensorService) { }

  ngOnInit() {
  }

  autoDiscover() {
    console.info('autoDiscover'); // TODO
  }

  public performSensorChanged(ev : any) {
    this.sensorChanged.emit(true);
  }
}
