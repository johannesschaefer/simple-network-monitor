import { Component, OnInit } from '@angular/core';
import { HostService } from '../../services/host.service';
import { Host } from '../../entities/host';
import { ActivatedRoute } from '../../../../node_modules/@angular/router';

@Component({
  selector: 'snm-host-detail',
  templateUrl: './host-detail.component.html',
  styleUrls: ['./host-detail.component.css']
})
export class HostDetailComponent implements OnInit {

  host : Host;

  error : any;

  constructor(private hostService : HostService, private route: ActivatedRoute) { }

  ngOnInit() {
    let id = this.route.snapshot.paramMap.get('id');
    this.hostService.get(id).subscribe(x => this.host = x, err => this.error = err);
  }

  public reload() {
    this.hostService.get(this.host.id).subscribe(x => this.host = x, err => this.error = err);
  }

  public performSensorChanged(ev : any) {
    this.reload();
  }
}
