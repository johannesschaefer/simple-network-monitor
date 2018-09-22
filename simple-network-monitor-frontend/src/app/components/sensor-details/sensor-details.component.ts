import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { Sensor } from '../../entities/sensor';
import { SampleService } from '../../services/sample.service';
import { LineChartComponent } from '@swimlane/ngx-charts';
import { Sample } from '../../entities/sample';
import { SampleType } from '../../entities/sampleType';
import { sample } from 'rxjs/operators';
import { SensorService } from '../../services/sensor.service';
import { Host } from '../../entities/host';

@Component({
  selector: 'snm-sensor-details',
  templateUrl: './sensor-details.component.html',
  styleUrls: ['./sensor-details.component.css']
})
export class SensorDetailsComponent implements OnInit {
  @Input()
  sensor : Sensor;

  @Output()
  sensorChanged = new EventEmitter<boolean>();

  @ViewChild('chart')
  chart : LineChartComponent;

  data : any = {};

  // options
  gradient = false;
  showLegend = true;

  // line, area
  autoScale = true;

  colorScheme = {
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };

  error : any[];

  static currentSelection = {};
  
//  @Input()
//  host : Host;

  constructor( private sampleService : SampleService, private sensorService : SensorService ) { }

  ngOnInit() {
    this.sensor.sampleTypes.forEach(type => {
      this.data[type.id] = {};

      this.sampleService.getBySensorAndType(this.sensor.id, type.id).subscribe(
        samples => {
          let seriesValue = {"name": 'value', "series": []};
          let seriesWarn = {"name": 'warn', "series": []};
          let seriesCritical = {"name": 'critical', "series": []};
          samples._embedded.samples.forEach( v => {
            let newValue = {"name": new Date(v.time), "value": v.value?v.value:0};
            let newWarn = {"name": new Date(v.time), "value": v.warn};
            let newCritical = {"name": new Date(v.time), "value": v.critical};
            if (v.min) {
              newValue["min"] = v.min;
            }
            if (v.max) {
              newValue["max"] = v.max;
            }
            seriesValue.series.push(newValue);
            seriesWarn.series.push(newWarn);
            seriesCritical.series.push(newCritical);
          });
          this.data[type.id] = [seriesValue];
          if (seriesWarn && seriesWarn.series[0].value) {
            this.data[type.id].push(seriesWarn);
          }
          if (seriesCritical && seriesCritical.series[0].value) {
            this.data[type.id].push(seriesCritical);
          }
          this.chart.update();
        },
        error => this.error = error
      );
    });
  }

  isActive(sampleType : SampleType) :boolean {
    if (typeof SensorDetailsComponent.currentSelection[this.sensor.id] === 'undefined') {
      this.setCurrentSampleType(sampleType);
    }
    return SensorDetailsComponent.currentSelection[this.sensor.id].id === sampleType.id;
  }

  setCurrentSampleType(sampleType : SampleType) {
    SensorDetailsComponent.currentSelection[this.sensor.id] = sampleType;
  }

  update() {
    this.chart.update();
  }

  delete(sensor : Sensor) {
    console.log('delete', sensor);
    this.sensorService.delete(sensor).subscribe( x => {
      console.log('deleted', x);
      this.sensorChanged.emit(true);
    }, err => {
      alert(err.message);
      console.log('deleted err', err);
    });
  }
}
