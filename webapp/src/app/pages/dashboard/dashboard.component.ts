import {Component, OnChanges, OnInit} from '@angular/core';
import {ApiService} from "../../core/api.service";
import api from "@fortawesome/fontawesome";
import {Issue, Reward} from "../../shared/model/Model";

@Component({
  selector: 'openmeed-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  issues: Issue[] =[];
  repositories = [];
  assignedIssues = [];
  admin = false;

  constructor(private http: ApiService) {
    if (sessionStorage.getItem("roles") === 'ROLE_USER') {
      this.admin = true;
    }
  }


  ngOnInit() {
    this.http.getRepositories().subscribe((repos) => {
      this.repositories = repos;
      console.log(this.repositories)
      this.repositories.reverse();

      this.repositories.forEach(entry => {
        this.http.getIssues(entry.fullName).subscribe((data:Issue[]) => {
          console.log(this.issues.length)
          data.forEach(value=>{
            this.issues.push(value)
          })
            //this.issues.concat(data);
            console.log(data)
          console.log(this.issues.length)

          this.http.getIssuesIncentives().subscribe(res => {
            res.forEach(entry => {
              document.getElementById(entry.issueId).innerText = entry.value.concat('pts')
            })
          });
          console.log(this.issues)
        });
      })
    });
  }

  allocateIncentiveToIssue(event) {
    var reward = new Reward();
    reward.value = event.target.value;
    reward.issueId = event.target.dataset.url;
    reward.type = "pts";

    let claimConstraints = new Map();
    claimConstraints.set("completionDate",new Date((new Date()).getTime() + (10 * 86400000)))
    reward.claimConstraints=claimConstraints;
    this.http.assignReward(reward).subscribe(result => {
        document.getElementById(event.target.dataset.url).innerText = reward.value.concat(" pts");
      }
    )
  }

}
