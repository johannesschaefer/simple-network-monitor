import { Component, OnInit } from '@angular/core';
import { HostService } from '../../services/host.service';

@Component({
  selector: 'snm-host-overview',
  templateUrl: './host-overview.component.html',
  styleUrls: ['./host-overview.component.css']
})
export class HostOverviewComponent implements OnInit {
  private total : number = null;
  private ok : number = null;
  private warn : number = null;
  private critical : number = null;
  private unknown : number = null;

  private error : any;

  constructor(private hostService : HostService) { }

  public ngOnInit() {
    this.reload();
  }

  public reload(){
    this.hostService.getOverview().subscribe(
      ovv => {
        this.total = ovv.total;
        this.ok = ovv.ok;
        this.warn = ovv.warn;
        this.critical = ovv.critical;
        this.unknown = ovv.unknown;
        this.error = null; },
      err => { 
        this.total = null;
        this.ok = null;
        this.warn = null;
        this.critical = null;
        this.unknown = null;
        this.error = err;
      }
     );
  }

}
