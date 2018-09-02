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
    // Initialize Params Object
    let Params = new HttpParams();

    // Begin assigning parameters
    if(page) {
      Params = Params.append('page', page.toString());
    }
    if(size) {
      Params = Params.append('size', size.toString());
    }
    if(sort) {
      sort.forEach(s => {Params = Params.append('sort', s.col);Params = Params.append(s.col + '.dir', s.direction); });
    }

    return this.http.get<SettingHal>( this.getUrl(), { params: Params });
  }

  public update(setting : Setting) : Observable<{}> {
    return this.http.post(this.getUrl() + 'update', setting);
  }
}
