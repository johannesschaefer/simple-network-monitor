import { Component, OnInit, Input, ViewChild, TemplateRef, EventEmitter, Output } from '@angular/core';
import { Sensor } from '../../entities/sensor';
import { CommandService } from '../../services/command.service';
import { Command } from '../../entities/command';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { SensorService } from '../../services/sensor.service';
import { Host } from '../../entities/host';

@Component({
  selector: 'snm-sensor-edit-add',
  templateUrl: './sensor-edit-add.component.html',
  styleUrls: ['./sensor-edit-add.component.css']
})
export class SensorEditAddComponent implements OnInit {
  @Input()
  tag : string;

  @Input()
  class : string;
  
  @Input()
  text : string;

  @Input()
  sensor : Sensor;

  @Input()
  host : Host;

  @Output()
  sensorChanged = new EventEmitter<boolean>();

  commandList : Command[];

  modalRef: BsModalRef;

  @ViewChild('addTpl')
  private addTempRef : TemplateRef<any>
  
  constructor(private modalService: BsModalService, private commandService : CommandService, private sensorService : SensorService) { 
    this.sensor = <Sensor>{ active: true, interval: 60000, command: null, properties: {}, secretProperties: {} };
  }

  ngOnInit() {
  }

  perform() {
    this.sensor.host = this.host;
    console.info('perform', this.sensor);
    this.sensorService.create(this.sensor).subscribe( x => {
      console.log('created/updated', x);
    }, err => {
      console.log('created/updated err', err);
      alert(err.message);
    },
    () => { 
      this.modalRef.hide();
      if(typeof this.sensor.id === 'undefined') {
        this.sensor = <Sensor>{ active: true, interval: 60000, command: null, properties: {}, secretProperties: {} };
      }
      this.sensorChanged.emit(true);
    }
    );
  }

  open() {
    this.commandService.getAll().subscribe(
      x => {
        this.commandList = x._embedded.commands;
        this.modalRef = this.modalService.show(this.addTempRef);
      }, err => {
        console.log('open err', err);
        alert(err.message);
      }
    );
  }
}