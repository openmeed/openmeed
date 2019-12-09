import {Component, OnInit} from '@angular/core';
import {ApiService} from "../../core/api.service";
import api from "@fortawesome/fontawesome";
import {Reward} from "../../shared/model/Model";

@Component({
  selector: 'openmeed-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  issues: [];
  repositories = [];
  assignedIssues = [];

  constructor(private http: ApiService) {
  }

  ngOnInit() {
    this.http.getAuthenticatedUserIssues().subscribe((data) => {
      this.assignedIssues = data;
    });

    this.http.getRepositories().subscribe((repos) => {
      this.repositories = repos;

      this.repositories.forEach(entry => {
        this.http.getIssues(entry).subscribe((data) => {
          if (data.length > 0) {
            this.issues = data;
          }
        });
      })
    });

  }

  workOnIssue(issue) {
    this.http.setUserIssue(issue.html_url).subscribe(res => {
    })
  }

  allocateIncentiveToIssue(event) {
    var reward = new Reward();
    reward.value = event.target.value;
    reward.issueId = event.target.dataset.url;
    reward.type = "pts";

    this.http.assignReward(reward).subscribe(result => {
        document.getElementById(event.target.id.concat("-reward")).innerText = reward.value.concat(" pts");
      }
    )
  }

}
