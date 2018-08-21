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
    return this.config.getBackendUrl() + '/sensors/';
  }
  
  public getAll(page?: number, size?: number, sort?: Sort[]): Observable<SensorHal> {
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

    return this.http.get<SensorHal>( this.getUrl(), { params: Params });
  }
  
  public get(id: string): Observable<Sensor> {
    return this.http.get<Sensor>(this.getUrl() + id);
  }
}
