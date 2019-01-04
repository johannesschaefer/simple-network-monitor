import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { HostService } from '../../services/host.service';
import { HostHal, Host } from '../../entities/host';
import { Status } from "../../entities/status";
import { Page } from '../../entities/page';
import { Sort } from '../../services/sort';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { ConfigurationService } from '../../services/configuration.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import {Observable,  forkJoin, of, interval } from 'rxjs';
import { Command } from '../../entities/command';
import { CommandService } from '../../services/command.service';

@Component({
  selector: 'snm-host-list',
  templateUrl: './host-list.component.html',
  styleUrls: ['./host-list.component.css']
})
export class HostListComponent implements OnInit {
  
  @BlockUI() blockUI: NgBlockUI;

  private hosts : HostHal = {_embedded: {hosts: new Array()}, page: null};
  error : any = null;
  private page: number = 0;
  size: number = 10;
  private sorts: Sort[] = Array({col: 'name', direction: 'asc'});

  modalRef: BsModalRef;

  private currentHost : Host = <Host>{};

  private discoveredHosts : Host[];

  private selectedHosts : any = {};

  @ViewChild('addTpl')
  private addTempRef : TemplateRef<any>

  @ViewChild('startAutoDiscoveryTpl')
  private startAutoDiscoveryTpl : TemplateRef<any>

  @ViewChild('autoDiscoverResultsTpl')
  private autoDiscoverResultsTpl : TemplateRef<any>

  private autoDiscoveryNetwork : string;
  private autoDiscoveryNetworks : string[] = [];

  commandList : Command[] = [];

  constructor( private commandService : CommandService, private hostService : HostService, private modalService: BsModalService, private config: ConfigurationService ) {
  }

  public ngOnInit() {
    this.reload();
  }

  public reload(){
    this.hostService.getAll(this.page, this.size, this.sorts).subscribe(
      hosts => { this.hosts = hosts; this.error = null; },
      err => { this.error = err; }
     );
  }

  public getSortIcon(col: string) : string {
    for (const s of this.sorts) {
      if (s.col == col && s.direction != null) {
        return s.direction == 'asc' ? 'fa-sort-up' : 'fa-sort-down';
      }
    }
    return "fa-sort";
  }

  public sort(col: string){
    let f = this.sorts.find(s => s.col == col);
    if(f !== undefined) {
      if (f.direction=='asc') {
        f.direction = 'desc' 
      }
      else {
        this.sorts.splice(this.sorts.indexOf(f), 1);
      }
    }
    else {
      this.sorts.push({col: col, direction: 'asc'});
    }
    this.reload();
  }

  public numberArray(value : number) : Array<number> {
    if(!value) return [];
    let res = [];
    for (let i = 0; i < value; i++) {
      res.push(i);
    }
    return res;
  }

  public changePageSize() {
    this.page = 0;
    this.reload();
  }

  public goto(page : number) {
    this.page = page;
    this.reload();
  }

  public prev() {
    this.page--;
    this.reload();
  }

  public next() {
    this.page++;
    this.reload();
  }

  public add() {
    console.info('add');
    this.currentHost = <Host>{properties: {}};
    this.commandService.getAll(0, 1000, [{col: 'name', direction: 'ASC'}]).subscribe(
      x => {
        this.commandList = x._embedded.commands;
        this.modalRef = this.modalService.show(this.addTempRef);
      }, err => {
        console.log('add err', err);
        alert(err.message);
      }
    );

  }

  public addPerform() {
    console.info('addPerform');
    this.modalRef.hide();
    this.hostService.create(this.currentHost).subscribe( x => {
      console.log('added', x);
      this.reload();
      this.currentHost = <Host>{};
    }, err => {
      console.log('added err', err);
      alert(err.message);
      this.currentHost = <Host>{};
    });
  }

  public edit(host: Host) {
    console.info('edit');
    this.currentHost = {...host};
    this.currentHost.commands = host.commands.map(x => Object.assign({}, x));
    this.commandService.getAll(0, 1000, [{col: 'name', direction: 'ASC'}]).subscribe(
      x => {
        this.commandList = x._embedded.commands;
        this.modalRef = this.modalService.show(this.addTempRef);
      }, err => {
        console.log('add err', err);
        alert(err.message);
      }
    );
  }

  public editPerform() {
    console.info('editPerform');
    this.modalRef.hide();
    this.hostService.update(this.currentHost).subscribe( x => {
      console.log('edited', x);
      this.reload();
      this.currentHost = <Host>{};
    }, err => {
      console.log('edited err', err);
      alert(err.message);
      this.currentHost = <Host>{};
    });
  }

  public delete(host : Host) {
    this.hostService.delete(host).subscribe( x => {
      this.reload();
      console.log('deleted', x);
    }, err => {
      alert(err.message);
      console.log('deleted err', err);
    });
  }

  public cancel() {
    this.modalRef.hide();
    this.currentHost = <Host>{};
  }

  public startAutoDiscovery() {
    console.info('startAutoDiscovery');
    this.config.getAutoDiscoveryNetwork().subscribe(
      x => {
        this.autoDiscoveryNetwork = x.length>0?x[0]:'';
        this.autoDiscoveryNetworks = x;
        this.modalRef = this.modalService.show(this.startAutoDiscoveryTpl);
      },
      err => {
        this.autoDiscoveryNetwork = '';
        this.autoDiscoveryNetworks = [];
        this.modalRef = this.modalService.show(this.startAutoDiscoveryTpl);
      }
    );
  }

  public performAutoDiscovery() {
    console.info('performAutoDiscovery');
    this.modalRef.hide();
    this.blockUI.start("Searching for hosts ...");
    this.hostService.autoDiscovery(this.autoDiscoveryNetwork).subscribe( hosts => {
      console.log('performAutoDiscovery', hosts);
      this.blockUI.stop();
      hosts.forEach( x => this.selectedHosts[x.name] = false);
      this.discoveredHosts = hosts;
      this.modalRef = this.modalService.show(this.autoDiscoverResultsTpl, {class: 'modal-lg'});
    }, err => {
      console.log('performAutoDiscovery err', err);
      this.blockUI.stop();
      alert(err.message);
    });
  }

  public addSelectedHosts() {
    console.log('addSelectedHosts', this.selectedHosts);
    
    let res : Observable<{}>[] = [];
    for (let index = 0; index < this.discoveredHosts.length; index++) {
      const h = this.discoveredHosts[index];
      if(this.selectedHosts[h.name]) {
        res.push(this.hostService.create(h));
      }
    }
    forkJoin(res).subscribe(x => this.reload());
    this.selectedHosts = [];
    this.modalRef.hide();
  }

  public export() {
    this.hostService.export();
  }

  public toggleADSelection(name : string) {
    if (this.selectedHosts.hasOwnProperty(name)) {
      this.selectedHosts[name] = !this.selectedHosts[name];
    }
    else {
      this.selectedHosts[name] = true;
    }
  }

  public toggleADSelectAll(e) {
    if(this.isADAllSelected()) {
      this.selectedHosts = {};
    }
    else {
      this.discoveredHosts.forEach( host => this.selectedHosts[host.name] = true );
    }
  }

  public isADAllSelected() : boolean {
    if (this.discoveredHosts.length == 0) {
      return false;
    }
    let i = 0;
    for (const host in this.selectedHosts) {
      if(this.selectedHosts[host]) {
        i++;
      }
    }

    return i == this.discoveredHosts.length;
  }

  public isADoneSelected() : boolean {
    for (const host in this.selectedHosts) {
      if(this.selectedHosts[host]) {
        return true;
      }
    }
    return false;
  }

  public addCommand() {
    if(typeof this.currentHost.commands === 'undefined') {
      this.currentHost.commands = [];
    }
    this.currentHost.commands.push(<Command>{});
  }

  public removeCommand( command : Command ) {
    const index = this.currentHost.commands.indexOf(command, 0);
    if (index > -1) {
      this.currentHost.commands.splice(index, 1);
    }
  }
}
