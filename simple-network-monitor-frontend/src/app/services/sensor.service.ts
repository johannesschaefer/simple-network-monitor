import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ConfigurationService } from './configuration.service';
import { Observable } from 'rxjs';
import { SensorHal, Sensor } from '../entities/sensor';
import { Sort } from './sort';

@Injectable({
  providedIn: 'root'
})
export class SensorService {

  constructor(private http: HttpClient, private config: ConfigurationService) {
  }

  private getUrl(){
    return this.config.getBackendUrl() + 'sensors/';
  }
  
  public getAll(page?: number, size?: number, sort?: Sort[]): Observable<SensorHal> {
    return this.http.get<SensorHal>( this.getUrl(), { params: this.config.getQueryParameters(page, size, sort) });
  }
  
  public get(id: string): Observable<Sensor> {
    return this.http.get<Sensor>(this.getUrl() + id);
  }

  public delete(sensor : Sensor) : Observable<{}> {
    return this.http.post(this.getUrl() + 'delete/' + sensor.id, null);
  }

  public create(sensor : Sensor) : Observable<{}> {
    return this.http.post(this.getUrl() + 'create', sensor);
  }

  public update(sensor : Sensor) : Observable<{}> {
    return this.http.post(this.getUrl(), sensor);
  }
}
