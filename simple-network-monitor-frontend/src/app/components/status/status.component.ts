import { Component, OnInit, Input } from '@angular/core';
import { Status } from '../../entities/status';

@Component({
  selector: 'snm-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {
  @Input()
  value : Status;

  constructor() { }

  public ngOnInit() {
  }

}
