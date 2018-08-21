import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { CommandService } from '../../services/command.service';
import { CommandHal, Command } from '../../entities/command';
import { Sort } from '../../services/sort';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

@Component({
  selector: 'snm-command-list',
  templateUrl: './command-list.component.html',
  styleUrls: ['./command-list.component.css']
})
export class CommandListComponent implements OnInit {

  private commands : CommandHal = {_embedded: {commands: new Array()}, page: null};
  private error : any = null;
  private page: number = 0;
  private size: number = 10;
  private sorts: Sort[] = [{col: 'name', direction: 'ASC'}];

  addModalRef: BsModalRef;

  private currentCmd : Command = <Command>{};

  @ViewChild('addTpl')
  private addTempRef : TemplateRef<any>

  constructor( private commandService : CommandService, private modalService: BsModalService ) { }

  ngOnInit() {
    this.reload();
  }

  public reload(){
    this.commandService.getAll(this.page, this.size, this.sorts).subscribe(
      commands => { this.commands = commands; this.error = null; },
      err => { this.error = err; }
     );
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

  public numberArray(value : number) : Array<number> {
    if(!value) return [];
    let res = [];
    for (let i = 0; i < value; i++) {
        res.push(i);
      }
    return res;
  }

  public add() {
    this.addModalRef = this.modalService.show(this.addTempRef);
    console.info('add');
  }

  public addCmd() {
    this.addModalRef.hide();
    this.commandService.create(this.currentCmd).subscribe( x => {
      this.reload();
      console.log('added', x);
      this.currentCmd = <Command>{};
    }, err => {
      alert(err.message);
      console.log('added err', err);
      this.currentCmd = <Command>{};
    });
  }

  public edit(command : Command) {
    this.currentCmd = {id: command.id, name: command.name, description: command.description, exec: command.exec};
    this.addModalRef = this.modalService.show(this.addTempRef);
    console.info('edit');
  }

  public editCmd() {
    this.addModalRef.hide();
    this.commandService.update(this.currentCmd).subscribe( x => {
      this.reload();
      console.log('deleted', x);
      this.currentCmd = <Command>{};
    }, err => {
      alert(err.message);
      console.log('deleted err', err);
      this.currentCmd = <Command>{};
    });
  }

  public delete(command : Command) {
    this.commandService.delete(command).subscribe( x => {
      this.reload();
      console.log('deleted', x);
    }, err => {
      alert(err.message);
      console.log('deleted err', err);
    });
  }

  public cancel() {
    this.addModalRef.hide();
    this.currentCmd = <Command>{};
  }

  public export() {
    console.log('export');
  }
}
