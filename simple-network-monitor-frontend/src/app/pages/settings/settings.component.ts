import { Component, OnInit } from '@angular/core';
import { SettingService } from '../../services/setting.service';

@Component({
  selector: 'snm-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  constructor( private settingService : SettingService ) { }

  ngOnInit() {
  }

  public reload() {
    
  }

  public startService() {
    console.log('startService');
    this.settingService.startScheduler();
  }
  public stopService() {
    console.log('stopService');
    this.settingService.stopScheduler();
  }
}
