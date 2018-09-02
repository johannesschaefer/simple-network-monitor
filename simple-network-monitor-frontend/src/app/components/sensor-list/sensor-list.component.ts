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

  private currentSensor : Sensor = <Sensor>{};

  private commandList : Command[];
  
  modalRef: BsModalRef;
  
  constructor(private modalService: BsModalService, private commandService : CommandService, private sensorService : SensorService) { }

  @ViewChild('addTpl')
  private addTempRef : TemplateRef<any>
  
  ngOnInit() {
  }

  autoDiscover() {
    console.info('autoDiscover'); // TODO
  }

  add() {
    console.info('add');
    this.commandService.getAll().subscribe(
      x => {
        this.commandList = x._embedded.commands;
        this.currentSensor = <Sensor>{ active: true, interval: 60000, command: null, properties: {}, secretProperties: {} };
        this.modalRef = this.modalService.show(this.addTempRef);
      }, err => {
        console.log('added err', err);
        alert(err.message);
      }
    );
  }

  public addPerform() {
    this.currentSensor.host = this.host;
    console.info('addPerform', this.currentSensor);
    this.modalRef.hide();
    this.sensorService.create(this.currentSensor).subscribe( x => {
      console.log('added', x);
      //this.reload();
      this.currentSensor = <Sensor>{};
    }, err => {
      console.log('added err', err);
      alert(err.message);
      this.currentSensor = <Sensor>{};
    });
  }

  public performSensorChanged(ev : any) {
    this.sensorChanged.emit(true);
  }
}
