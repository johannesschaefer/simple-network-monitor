import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'snm-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  public reload() {
    
  }

  public startService() {
    console.log('startService');
  }
  public stopService() {
    console.log('stopService');
  }
}
