import { Injectable } from '@angular/core';
import { ConfigurationService } from './configuration.service';
import { Sort } from './sort';
import { Observable } from 'rxjs';
import { HttpParams, HttpClient } from '@angular/common/http';
import { SettingHal, Setting } from '../entities/setting';

@Injectable({
  providedIn: 'root'
})
export class SettingService {
  constructor(private http: HttpClient, private config: ConfigurationService) {
  }

  private getUrl(){
    return this.config.getBackendUrl() + 'settings/';
  }
  
  public getAll(page?: number, size?: number, sort?: Sort[]): Observable<SettingHal> {
    return this.http.get<SettingHal>( this.getUrl(), { params: this.config.getQueryParameters(page, size, sort) });
  }

  public update(setting : Setting) : Observable<{}> {
    return this.http.post(this.getUrl() + 'update', setting);
  }

  public export() {
    window.open(this.getUrl() + 'export');
    /*
    this.http.get<string>( this.getUrl() + 'export' ).subscribe(x => {
      var blob = new Blob([x], { type: 'text/json' });
      var url= window.URL.createObjectURL(blob);
      window.open(url);
    }, err => alert(err));*/
  }
}
