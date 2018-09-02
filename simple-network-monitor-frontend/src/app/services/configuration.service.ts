import { Injectable } from '@angular/core';
import { isDevMode } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Setting, SettingHal } from '../entities/setting';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor(private http: HttpClient) { }

  getBackendUrl() : string {
    return environment.url;
  }
  
  private async delay(ms: number) {
    await new Promise( resolve => setTimeout(resolve, ms) );
  }

  getAutoRefreshInterval() : Observable<number> {
    return this.http.get<Setting>(this.getBackendUrl() + 'settings/refreshInterval').map(x => parseInt(x.value));
  }

  getAutoDiscoveryNetwork() : Observable<string[]> {
    return this.http.get<string[]>(this.getBackendUrl() + 'settings/networks');
  }
}
