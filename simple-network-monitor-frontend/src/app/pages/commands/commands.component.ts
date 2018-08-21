import { Component, OnInit, ViewChild } from '@angular/core';
import { CommandListComponent } from '../../components/command-list/command-list.component';

@Component({
  selector: 'snm-commands',
  templateUrl: './commands.component.html',
  styleUrls: ['./commands.component.css']
})
export class CommandsComponent implements OnInit {

  @ViewChild('cmdList')
  private cmdList : CommandListComponent;

  constructor() { }

  ngOnInit() {
  }

  public reload() {
    this.cmdList.reload();
  }

}
