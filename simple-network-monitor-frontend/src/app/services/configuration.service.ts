import { Injectable } from '@angular/core';
import { isDevMode } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor() { }

  getBackendUrl() : string {
    return environment.url;
    /*
    if (isDevMode()) {
      return '//localhost:8080/';
    }
    return "//" + window.location.host + "/"
    */
  }

  getAutoRefreshInterval() : number {
    return 10000;
  }

  getAutoDiscoveryNetwork() : string {
    return "192.168.178.0/24";
  }
}
