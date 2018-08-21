import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Sensor } from '../../entities/sensor';
import { SampleService } from '../../services/sample.service';
import { LineChartComponent } from '@swimlane/ngx-charts';

@Component({
  selector: 'snm-sensor-details',
  templateUrl: './sensor-details.component.html',
  styleUrls: ['./sensor-details.component.css']
})
export class SensorDetailsComponent implements OnInit {
  @Input()
  sensor : Sensor;

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

  constructor( private sampleService : SampleService ) { }

  ngOnInit() {
    this.sensor.sampleTypes.forEach(type => {
      this.data[type.id] = {};

      this.sampleService.getBySensorAndType(this.sensor.id, type.id).subscribe(
        samples => {
          let series = {"name": type.name, "series": []};
          samples._embedded.samples.forEach( v => {
            let newValue = {"name": new Date(v.time), "value": v.value?v.value:0};
            series.series.push(newValue);
          });
          this.data[type.id] = [series];
          this.chart.update();
        },
        error => this.error = error
      );
    });
  }

  update() {
    this.chart.update();
  }
}
