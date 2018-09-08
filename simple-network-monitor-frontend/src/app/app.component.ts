import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable, Subscription, timer } from 'rxjs';
import { ConfigurationService } from './services/configuration.service';

@Component({
  selector: 'snm-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private configService : ConfigurationService) { }

  content : any;

  private autoRefreshTimer :  Observable<number>;
  private autoRefreshSubscribtion : Subscription;

  ngOnInit() {
    this.configService.getAutoRefreshInterval().subscribe(x => this.startAutorefresh(x), err => alert(err));
  }

  private startAutorefresh(ms : number) {
    this.autoRefreshTimer = timer(ms, ms);
    this.autoRefreshSubscribtion = this.autoRefreshTimer.subscribe(v => this.reload());
  }

  public toogleAutoRefresh() {
    if(this.autoRefreshSubscribtion && this.autoRefreshSubscribtion.closed) {
      this.autoRefreshSubscribtion = this.autoRefreshTimer.subscribe(v => this.reload());
    }
    else {
      this.autoRefreshSubscribtion.unsubscribe();
    }
  }

  public isAutoRefreshActive() : boolean {
    return this.autoRefreshSubscribtion && !this.autoRefreshSubscribtion.closed;
  }

  reload() {
    if(typeof this.content.reload === 'function') {
      this.content.reload();
    }
  }

  public onRouterOutletActivate(event : any) {
    this.content = event;
  }

  public getVersion() : string {
    return this.configService.getVersion();
  }

  public getCommit() : string {
    return this.configService.getCommit();
  }
}
