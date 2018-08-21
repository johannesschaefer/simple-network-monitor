import { Component, ViewChild, OnInit } from '@angular/core';
import { HostListComponent } from '../../components/host-list/host-list.component';
import { HostOverviewComponent } from '../../components/host-overview/host-overview.component';
import { interval, timer, Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators'
import { ConfigurationService } from '../../services/configuration.service';

@Component({
  selector: 'app-root',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  @ViewChild('list')
  private hostList : HostListComponent;

  @ViewChild('ovv')
  private hostOvv : HostOverviewComponent;

  constructor() { }

  public ngOnInit() {
  }

  public reload() {
    this.hostList.reload();
    this.hostOvv.reload();
  }
}
