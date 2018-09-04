import { Component, OnInit } from '@angular/core';
import { SettingService } from '../../services/setting.service';
import { SettingHal } from '../../entities/setting';

@Component({
  selector: 'snm-settings-list',
  templateUrl: './settings-list.component.html',
  styleUrls: ['./settings-list.component.css']
})
export class SettingsListComponent implements OnInit {

  settings : SettingHal = <SettingHal>{};

  constructor( private settingService : SettingService ) { }

  ngOnInit() {
    this.settingService.getAll(null, null, [{col: 'name', direction: 'ASC'}] ).subscribe( x => this.settings = x, err => { alert(err) });
  }

  save() {
    for (let s of this.settings._embedded.settings) {
      this.settingService.update(s).subscribe( x => console.log(x), err => alert(err) );
    }
  }

  export() {
    this.settingService.export();
  }

}
