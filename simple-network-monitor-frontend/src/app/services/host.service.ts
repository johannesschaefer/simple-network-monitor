import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ConfigurationService } from './configuration.service';
import { HostHal, Host } from '../entities/host';
import { Sort } from './sort';
import { CommandResult } from '../entities/commandResult';
import { HostOverview } from './hostOverview';



@Injectable({
  providedIn: 'root'
})
export class HostService {

  constructor(private http: HttpClient, private config: ConfigurationService) {
  }

  private getUrl(){
    return this.config.getBackendUrl() + 'hosts/';
  }

  public getAll(page?: number, size?: number, sort?: Sort[]): Observable<HostHal> {
    return this.http.get<HostHal>( this.getUrl(), { 'params': this.config.getQueryParameters(page, size, sort) });
  }
  
  public get(id: string): Observable<Host> {
    return this.http.get<Host>(this.getUrl() + id);
  }

  public runCommand(hostId: string, commandId:string) : Observable<CommandResult> {
    return this.http.get<CommandResult>(this.getUrl() + hostId + '/commands/' + commandId + '/run');
  }

  public getOverview() : Observable<HostOverview> {
    return this.http.get<HostOverview>(this.config.getBackendUrl() + '/hostoverview');
  }

  public delete(host : Host) : Observable<{}> {
    return this.http.post(this.getUrl() + 'delete/' + host.id, null);
  }

  public create(host : Host) : Observable<{}> {
    return this.http.post(this.getUrl() + 'create', host);
  }

  public update(host : Host) : Observable<{}> {
    return this.http.post(this.getUrl() + 'create', host);
//    return this.http.patch(this.getUrl() + host.id, host);
  }

  public autoDiscovery(network : string) : Observable<Host[]> {
    let Params = new HttpParams();
    Params.append('network', network);
    return this.http.get<Host[]>(this.getUrl() + 'autodiscovery', { 'params': { 'network': network } });
  }

  public export() {
    window.open(this.getUrl() + 'export');/*
    var blob = new Blob(["{data:123}"], { type: 'text/json' });
    var url= window.URL.createObjectURL(blob);
    window.open(url);*/
  }
}