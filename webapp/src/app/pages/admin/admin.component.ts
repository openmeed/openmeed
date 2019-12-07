import {Component, OnInit} from '@angular/core';
import {HttpEventType, HttpResponse} from '@angular/common/http';
import {ApiService} from '../../core/api.service';

@Component({
  selector: 'openmeed-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  reposToActivate = [];
  projects

  constructor(private http: ApiService) {
  }

  ngOnInit() {
    this.http.fetchAllRepositories().subscribe( data=>{
      console.log(data)
      this.projects = data;
    })
  }

  activateRepository(value){
    if(!this.reposToActivate.includes(value)){
      this.reposToActivate.push(value);
    }else{
      this.reposToActivate.splice(this.reposToActivate.indexOf(value))
    }
  }


}
