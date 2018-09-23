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

  commands : CommandHal = {_embedded: {commands: new Array()}, page: null};
  error : any = null;
  page: number = 0;
  size: number = 10;
  sorts: Sort[] = [{col: 'name', direction: 'ASC'}];

  addModalRef: BsModalRef;

  currentCmd : Command = <Command>{};

  icons : String[] = [];

  @ViewChild('addTpl')
  addTempRef : TemplateRef<any>

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

  public changePageSize() {
    this.page = 0;
    this.reload();
  }


  public add() {
    this.commandService.getIcons().subscribe(
      x => {
        this.icons = x;
        this.addModalRef = this.modalService.show(this.addTempRef);
        console.info('add');
      },
      err => {
        alert(err.message);
        console.log('add err', err);
      }
    );
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
    this.commandService.getIcons().subscribe(
      x => {
        this.icons = x;
        this.currentCmd = {id: command.id, name: command.name, description: command.description, exec: command.exec, icon: command.icon};
        this.addModalRef = this.modalService.show(this.addTempRef);
        console.info('edit');
      },
      err => {
        alert(err.message);
        console.log('add err', err);
      }
    );
  }

  public editCmd() {
    this.addModalRef.hide();
    this.commandService.update(this.currentCmd).subscribe( x => {
      this.reload();
      console.log('editCmd', x);
      this.currentCmd = <Command>{};
    }, err => {
      alert(err.message);
      console.log('editCmd err', err);
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
    this.commandService.export();
  }
}
