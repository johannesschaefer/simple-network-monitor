import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor() { }

  getBackendUrl() : string {
    return '//localhost:8080/';
  }

  getAutoRefreshInterval() : number {
    return 10000;
  }

  getAutoDiscoveryNetwork() : string {
    return "192.168.178.0/24";
  }
}
