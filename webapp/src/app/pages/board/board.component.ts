import {Component, Directive, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren} from '@angular/core';
import {Model} from "../../shared/model/Model";
import {ApiService} from "../../core/api.service";

@Component({
  selector: 'openmeed-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  users = [];
  selectedUser: Model;

  constructor(private api:ApiService) { }

  ngOnInit() {
    var user = new Model();
    user.name = 'Eben Graham';
    user.points = 10;
    this.users.push(user)
    this.api.getUsersForLeaderbaord().subscribe( data=>{
      console.log(data)
      this.users.push(data);
    })
  }

  onSelect(user: Model): void {
    this.selectedUser = user;
  }
}
