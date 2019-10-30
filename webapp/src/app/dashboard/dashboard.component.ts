import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpResponse, HttpEventType} from '@angular/common/http';
import {HttpService} from "../services/http.service";
import {Issue} from "../shared/modal/Issue";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  issues: Issue[];

  constructor(private http: HttpService) {
  }

  ngOnInit() {
    this.http.getIssues().subscribe((data) => {
      this.issues = data;
    });
  }

  selectIssue(id) {
    this.http.assignIssue(localStorage.getItem('memberid'), id);
  }


}
