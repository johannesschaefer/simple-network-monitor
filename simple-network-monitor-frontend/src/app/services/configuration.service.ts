
import {map} from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { isDevMode } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Setting, SettingHal } from '../entities/setting';
import { Observable } from 'rxjs';
import { Sort } from './sort';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor(private http: HttpClient) { }

  public getBackendUrl() : string {
    return environment.url;
  }

  public getVersion() : string {
    return environment.version;
  }

  public getCommit() : string {
    return environment.commit;
  }

  public getAutoRefreshInterval() : Observable<number> {
    return this.http.get<Setting>(this.getBackendUrl() + 'settings/refreshInterval').pipe(map(x => parseInt(x.value)));
  }

  public getAutoDiscoveryNetwork() : Observable<string[]> {
    return this.http.get<string[]>(this.getBackendUrl() + 'settings/networks');
  }
  
  public getQueryParameters(page?: number, size?: number, sort?: Sort[]) : HttpParams {
    let params = new HttpParams();

    if(page) {
      params = params.append('page', page.toString());
    }
    else {
      params = params.append('page', '0');
    }

    if(size) {
      params = params.append('size', size.toString());
    }
    else {
      params = params.append('page', '1000');
    }

    if(sort){
      sort.forEach(s => {params = params.append('sort', s.col + ',' + s.direction); }); // Params = Params.append(s.col + '.dir', s.direction);
    }
    return params;
  }
}
