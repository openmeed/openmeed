import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../core/api.service";

@Component({
  selector: 'openmeed-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  issues: any[];
  assignedIssues: any[];

  constructor(private http: ApiService) {
  }

  ngOnInit() {
    this.http.getAuthenticatedUserIssues().subscribe((data) => {
      this.assignedIssues = data;
    });

    this.http.getIssues("anoited007", "personalized-tab").subscribe((data) => {
      this.issues = data;
    });


  }

}
