import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigurationService } from './configuration.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {
  constructor(private http: HttpClient, private config: ConfigurationService) {
  }

  private getUrl(){
    return this.config.getBackendUrl() + 'schedule/';
  }

  public start() : Observable<void> {
    return this.http.get<void>(this.getUrl() + '/start');
  }

  public stop() : Observable<void> {
    return this.http.get<void>(this.getUrl() + '/stop');
  }

  public isRunning() : Observable<boolean> {
    return this.http.get<boolean>(this.getUrl() + '/isrunning');
  }
}
