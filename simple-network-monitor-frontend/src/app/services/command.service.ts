import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ConfigurationService } from './configuration.service';
import { Observable } from 'rxjs';
import { CommandHal, Command } from '../entities/command';
import { CommandResult } from '../entities/commandResult';
import { Sort } from './sort';

@Injectable({
  providedIn: 'root'
})
export class CommandService {

  constructor(private http: HttpClient, private config: ConfigurationService) {
  }

  private getUrl(){
    return this.config.getBackendUrl() + '/commands/';
  }
  
  public getAll(page?: number, size?: number, sort?: Sort[]): Observable<CommandHal> {
    // Initialize Params Object
    let Params = new HttpParams();

    // Begin assigning parameters
    if(page) {
      Params = Params.append('page', page.toString());
    }
    if(size) {
      Params = Params.append('size', size.toString());
    }
    if(sort){
      sort.forEach(s => {Params = Params.append('sort', s.col);Params = Params.append(s.col + '.dir', s.direction); });
    }

    return this.http.get<CommandHal>( this.getUrl(), { params: Params });
  }
  
  public get(id: string): Observable<Command> {
    return this.http.get<Command>(this.getUrl() + id);
  }

  public delete(command : Command) : Observable<{}> {
    return this.http.delete(this.getUrl() + command.id);
  }

  public create(command : Command) : Observable<{}> {
    return this.http.post(this.getUrl(), command);
  }

  public update(command : Command) : Observable<{}> {
    return this.http.patch(this.getUrl() + command.id, command);
  }
  
}
