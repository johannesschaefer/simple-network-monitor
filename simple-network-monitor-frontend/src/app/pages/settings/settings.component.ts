import { Component, OnInit } from '@angular/core';
import { SettingService } from '../../services/setting.service';
import { ScheduleService } from '../../services/schedule.service';

@Component({
  selector: 'snm-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  
  schedulerRunning : boolean = null;

  constructor( private settingService : SettingService, private scheduleService : ScheduleService ) { }

  ngOnInit() {
    this.reload();
  }

  public reload() {
    this.scheduleService.isRunning().subscribe( x => this.schedulerRunning = x, err => this.schedulerRunning = null );
  }

  public startService() {
    console.log('startService');
    this.scheduleService.start().subscribe( x => this.reload(), err => alert(err) );
  }

  public stopService() {
    console.log('stopService');
    this.scheduleService.stop().subscribe( x => this.reload(), err => alert(err) );
  }
}
