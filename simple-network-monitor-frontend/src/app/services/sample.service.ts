import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ConfigurationService } from './configuration.service';
import { Observable } from 'rxjs';
import { SampleHal, Sample } from '../entities/sample';
import { Sort } from './sort';

@Injectable({
  providedIn: 'root'
})
export class SampleService {

  constructor(private http: HttpClient, private config: ConfigurationService) {
  }

  private getUrl(){
    return this.config.getBackendUrl() + '/samples/';
  }
  
  public getAll(page?: number, size?: number, sort?: Sort[]): Observable<SampleHal> {
    // Initialize Params Object
    let params = new HttpParams();

    // Begin assigning parameters
    if(page) {
      params = params.append('page', page.toString());
    }
    if(size) {
      params = params.append('size', size.toString());
    }
    if(sort) {
      sort.forEach(s => {params = params.append('sort', s.col);params = params.append(s.col + '.dir', s.direction); });
    }

    return this.http.get<SampleHal>( this.getUrl(), { 'params': params });
  }
  
  // http://localhost:8080/samples/search/findBySensorAndType?sensorId=7fd37590-bcc8-4b6f-80a5-9a4e605e5611&typeId=e0bf4709-10d4-4e94-8725-82912e5bcba0
  public getBySensorAndType(sensorId : string, typeId : string): Observable<SampleHal> {
    return this.http.get<SampleHal>( this.getUrl() + 'search/findBySensorAndType', { 'params': {'sensorId': sensorId, 'typeId': typeId} });
  }
  
  public get(id: string): Observable<Sample> {
    return this.http.get<Sample>(this.getUrl() + id);
  }
}
