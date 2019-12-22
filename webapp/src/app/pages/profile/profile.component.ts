import {Component, OnInit} from '@angular/core';
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
  total_points;

  constructor(private http: ApiService) {
  }

  ngOnInit() {
    /* this.http.getAuthenticatedUserIssues().subscribe((data) => {
       this.issues = data;
       sessionStorage.setItem("username",data.login);
     });*/

    this.http.getUser().subscribe((data) => {
      this.user = data;
      this.http.getUserProfile(this.user.email).subscribe((data) => {
        this.total_points = data.points;
        this.issues = data.issues;
      });
    });


  }

}
