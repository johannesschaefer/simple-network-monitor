import { Component, OnInit, Input, TemplateRef, ViewChild } from '@angular/core';
import { Command } from '../../entities/command';
import { CommandService } from '../../services/command.service';
import { HostService } from '../../services/host.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { Status } from '../../entities/status';
import { Host } from '../../entities/host';

@Component({
  selector: 'snm-command-starter',
  templateUrl: './command-starter.component.html',
  styleUrls: ['./command-starter.component.css']
})
export class CommandStarterComponent implements OnInit {
  @Input()
  host : Host;

  modalRef: BsModalRef;

  msg : string;
  
  status : Status;

  currentCommand : Command;
  
  @ViewChild('confirm')
  confirmTempRef : TemplateRef<any>

  @ViewChild('result')
  resultTempRef : TemplateRef<any>

  constructor(private hostService : HostService, private dialog : BsModalService) { }

  public ngOnInit() {
  }

  public run(host:Host, command : Command) {
    this.currentCommand = command;
    this.modalRef = this.dialog.show(this.confirmTempRef);
  }

  public run2(host:Host, command : Command) {
    this.modalRef.hide();
    this.hostService.runCommand(host.id, command.id).subscribe(x=>{
      this.msg = x.message;
      this.status = x.status;
      this.modalRef = this.dialog.show(this.resultTempRef);
    });
  }
}
