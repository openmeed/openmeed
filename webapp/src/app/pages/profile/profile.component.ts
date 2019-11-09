import { Component, OnInit } from '@angular/core';
import {Profile} from '../../shared/model/Profile';
import {ApiService} from "../../core/api.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  user = new Profile();
  issues = [];
  constructor(private http:ApiService) { }

  ngOnInit() {
    this.http.getAuthenticatedUserIssues().subscribe((data) => {
      this.issues = data;
    });

    this.http.getUser().subscribe((data) => {
      this.user = data;
    });
  }

}
