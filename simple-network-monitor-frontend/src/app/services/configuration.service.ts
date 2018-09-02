import { Injectable } from '@angular/core';
import { isDevMode } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor(private http: HttpClient) { }

  getBackendUrl() : string {
    return environment.url;
  }

  getAutoRefreshInterval() : number {
    return 10000; // TODO: load from DB
  }

  getAutoDiscoveryNetwork() : string {
    return "192.168.178.0/24"; // TODO: discover the correct value automatically
  }
}
